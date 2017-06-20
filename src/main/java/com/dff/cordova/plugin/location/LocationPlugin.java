package com.dff.cordova.plugin.location;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.HandlerThread;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dff.cordova.plugin.common.CommonPlugin;
import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.common.service.CommonServicePlugin;
import com.dff.cordova.plugin.common.service.ServiceHandler;
import com.dff.cordova.plugin.location.actions.index.IndexActions;
import com.dff.cordova.plugin.location.broadcasts.ChangeProviderReceiver;
import com.dff.cordova.plugin.location.broadcasts.NewLocationReceiver;
import com.dff.cordova.plugin.location.classes.Executor;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.dagger.annotations.LocationRequestHandlerThread;
import com.dff.cordova.plugin.location.dagger.components.DaggerPluginComponent;
import com.dff.cordova.plugin.location.dagger.components.PluginComponent;
import com.dff.cordova.plugin.location.dagger.modules.ActivityModule;
import com.dff.cordova.plugin.location.dagger.modules.AppModule;
import com.dff.cordova.plugin.location.dagger.modules.CordovaModule;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.services.LocationService;
import com.dff.cordova.plugin.location.services.PendingLocationsIntentService;
import com.dff.cordova.plugin.location.utilities.helpers.FileHelper;
import com.dff.cordova.plugin.location.utilities.helpers.MessengerHelper;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;

import javax.inject.Inject;

/**
 * Cordova Plugin class that deals with the android's Location API, in order to get the location of the device as
 * well as to persist the locations when the app is not reachable.
 *
 * @author Anthony Nahas
 * @version 8.0.1
 * @since 28.11.2016
 */
public class LocationPlugin extends CommonServicePlugin {

    private static final String TAG = "LocationPlugin";
    private static final String[] LOCATION_PERMISSIONS =
        {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        };

    private static PluginComponent sComponent;

    @Inject
    @ApplicationContext
    Context mContext;

    @Inject
    @LocationRequestHandlerThread
    HandlerThread mHandlerThread;

    @Inject
    ServiceHandler mServiceHandler;

    @Inject
    Executor mExecutor;

    @Inject
    FileHelper mFileHelper;

    @Inject
    MessengerHelper mMessengerHelper;

    @Inject
    PreferencesHelper mPreferencesHelper;

    @Inject
    IndexActions mIndex;

    private ChangeProviderReceiver mChangeProviderReceiver;

    private NewLocationReceiver mNewLocationReceiver;
    private IntentFilter mNewLocationIntentFilter;

    private IntentFilter mChangeProviderIntentFilter;

    private CordovaInterface mCordovaInterface;

    /**
     * Def-Constructor
     */
    public LocationPlugin() {
        super(TAG);
    }

    /**
     * Initialization of the plugin and the private properties.
     * and respectively bind and start the location service.
     */
    @Override
    public void pluginInitialize() {

        //Instantiating the component
        sComponent = DaggerPluginComponent.builder()
            // list of modules that are part of this component need to be created here too
            .appModule(new AppModule(cordova.getActivity().getApplication()))
            .cordovaModule(new CordovaModule(cordova))
            .activityModule(new ActivityModule(cordova.getActivity()))
            .build();

        sComponent.inject(this);

        mCordovaInterface = cordova;

        requestLocationPermission();
        mContext.stopService(new Intent(mContext, LocationService.class));

        super.pluginInitialize(mServiceHandler);
        init();
    }


    /**
     * Executes an action called by JavaScript
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return true value if the action found, otherwise false.
     * @throws JSONException
     */
    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {

        if (action != null) {
            mCordovaInterface.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Action = " + action);
                    switch (action) {
                        case LocationResources.ACTION_START_SERVICE:
                            mExecutor.execute(
                                mIndex.mStartLocationServiceAction
                                    .with(callbackContext)
                                    .andHasArguments(args)
                            );
//                            mExecutor.startLocationService(mHandlerThread, mServiceHandler, args, callbackContext);

                            break;
                        case LocationResources.ACTION_STOP_SERVICE:

                            mExecutor.stopLocationService(mHandlerThread, mServiceHandler, callbackContext);
                            break;

                        case LocationResources.ACTION_GET_LOCATION:
                            mExecutor.getLocation(callbackContext, mHandlerThread, mServiceHandler, args);
                            break;

                        case LocationResources.ACTION_GET_LOCATION_LIST:
                            mExecutor.getLocationList(callbackContext, args);
                            break;

                        case LocationResources.ACTION_ENABLE_MAPPING_LOCATIONS:
                            LocationResources.IS_TO_CALCULATE_DISTANCE = true;
                            callbackContext.success();
                            break;

                        case LocationResources.ACTION_GET_TOTAL_DISTANCE_CALCULATOR:
                            mExecutor.getTotalDistance(callbackContext, args);
                            break;

                        case LocationResources.ACTION_SET_STOP_ID:
                        case LocationResources.ACTION_GET_LAST_STOP_ID:
                        case LocationResources.ACTION_CLEAR_STOP_ID:
                            mExecutor.handleStopId(action, args, callbackContext);
                            break;

                        case LocationResources.ACTION_GET_KEY_SET_FROM_LOCATIONS_MULTI_MAP:
                            mExecutor.getKeySetFromLocationsMultimap(callbackContext);
                            break;

                        case LocationResources.ACTION_REGISTER_PROVIDER_LISTENER:
                            mChangeProviderReceiver = new ChangeProviderReceiver(callbackContext);
                            mChangeProviderIntentFilter = new IntentFilter(LocationResources.BROADCAST_ACTION_ON_CHANGED_PROVIDER);
                            LocalBroadcastManager.getInstance(mContext).registerReceiver(mChangeProviderReceiver, mChangeProviderIntentFilter);
                            break;

                        case LocationResources.ACTION_UNREGISTER_PROVIDER_LISTENER:
                            if (mChangeProviderReceiver != null) {
                                LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mChangeProviderReceiver);
                                callbackContext.success();
                            }
                            break;

                        case LocationResources.ACTION_SET_STOP_LISTENER:
                            mExecutor.setStopListener(callbackContext, args);
                            break;
                        case LocationResources.ACTION_CANCEL_STOP_LISTENER:
                            mExecutor.stopStopListener();
                            break;
                        case LocationResources.ACTION_SET_LOCATION_LISTENER:
                            int type = 1;
                            try {
                                if (args.get(0) != null) {
                                    type = args.getInt(0);
                                }
                            } catch (JSONException e) {
                                CordovaPluginLog.e(TAG, "Error: ", e);
                            }

                            mNewLocationReceiver = new NewLocationReceiver(callbackContext, type);
                            mNewLocationIntentFilter = new IntentFilter(LocationResources.BROADCAST_ACTION_ON_NEW_LOCATION);
                            LocalBroadcastManager.getInstance(mContext).
                                registerReceiver(mNewLocationReceiver, mNewLocationIntentFilter);
                            break;
                        default:
                            break;

                             /*
                    else if (action.equals(LocationResources.ACTION_INTENT_STORE_PENDING_LOCATIONS) ||
                            action.equals(LocationResources.ACTION_INTENT_RESTORE_PENDING_LOCATIONS)) {
                        Intent pendingLocationsIntentService = new Intent(mContext, PendingLocationsIntentService.class);
                        pendingLocationsIntentService.setAction(action);
                        mContext.startService(pendingLocationsIntentService);
                    }
                    */
                    }
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // TODO: 05.05.2017 assert != null
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mNewLocationReceiver);
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mChangeProviderReceiver);

        mFileHelper.storePendingLocation();
        mFileHelper.storeLocationsMultimap();
    }

    /**
     * Inject the target object into dagger
     *
     * @param object
     * @param <T>
     */
    public static <T extends Service> void inject(T object) {
        if (object instanceof LocationService || object instanceof PendingLocationsIntentService) {
            if (LocationPlugin.sComponent != null) {
                if (object instanceof LocationService) {
                    LocationPlugin.sComponent.inject((LocationService) object);
                }
                if (object instanceof PendingLocationsIntentService) {
                    LocationPlugin.sComponent.inject((PendingLocationsIntentService) object);
                }
            } else {
                LocationPlugin.sComponent = DaggerPluginComponent
                    .builder()
                    .appModule(new AppModule(object.getApplication()))
                    .build();
            }
            return;
        }
        Log.w(TAG, "Trying to inject a  unproved object into Dagger --> " + object.getClass());
    }

    /**
     * request permissions if they are not granted by forwarding them to
     * the common plugin
     */
    private void requestLocationPermission() {
        for (String permission : LOCATION_PERMISSIONS) {
            CommonPlugin.addPermission(permission);
        }
    }

    private void init() {
//        mMessengerHelper.setMessenger(mServiceHandler.getService());
        mExecutor.execute(mIndex.mRestoreAction);
    }

    private void restore() {
        mPreferencesHelper.restoreProperties();
        mPreferencesHelper.setIsServiceStarted(false);
//        mExecutor.restore();
    }

    public CordovaInterface getCordovaInterface() {
        return mCordovaInterface;
    }

    public void setCordovaInterface(CordovaInterface mCordovaInterface) {
        this.mCordovaInterface = mCordovaInterface;
    }
}
