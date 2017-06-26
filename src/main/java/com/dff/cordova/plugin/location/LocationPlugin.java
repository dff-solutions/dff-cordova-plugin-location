package com.dff.cordova.plugin.location;

import android.Manifest;

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
import com.dff.cordova.plugin.location.broadcasts.ChangeProviderReceiver;
import com.dff.cordova.plugin.location.broadcasts.NewLocationReceiver;
import com.dff.cordova.plugin.location.classes.Executor;
import com.dff.cordova.plugin.location.configurations.ActionsManager;
import com.dff.cordova.plugin.location.dagger.DaggerManager;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.dagger.annotations.LocationRequestHandlerThread;
import com.dff.cordova.plugin.location.resources.Res;
import com.dff.cordova.plugin.location.services.LocationService;
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
    ActionsManager mActionsManager;

    @Inject
    FileHelper mFileHelper;

    @Inject
    MessengerHelper mMessengerHelper;

    @Inject
    PreferencesHelper mPreferencesHelper;

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

        DaggerManager
            .getInstance()
            .in(cordova.getActivity().getApplication())
            .and(cordova)
            .inject(this);

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

                    try {
                        if (mActionsManager.allJSAction().contains(action)) {
                            mExecutor.execute
                                (
                                    mActionsManager
                                        .hash(action)
                                        .with(callbackContext)
                                        .andHasArguments(args)
                                );
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error: --> ", e);
                    }

                    case Res.ACTION_REGISTER_PROVIDER_LISTENER:
                    mChangeProviderReceiver = new ChangeProviderReceiver(callbackContext);
                    mChangeProviderIntentFilter = new IntentFilter(Res.BROADCAST_ACTION_ON_CHANGED_PROVIDER);
                    LocalBroadcastManager.getInstance(mContext).registerReceiver(mChangeProviderReceiver, mChangeProviderIntentFilter);
                    break;

                    case Res.ACTION_UNREGISTER_PROVIDER_LISTENER:
                    if (mChangeProviderReceiver != null) {
                        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mChangeProviderReceiver);
                        callbackContext.success();
                    }
                    break;

                    case Res.ACTION_SET_STOP_LISTENER:
                    mExecutor.setStopListener(callbackContext, args);
                    break;
                    case Res.ACTION_CANCEL_STOP_LISTENER:
                    mExecutor.stopStopListener();
                    break;
                    case Res.ACTION_SET_LOCATION_LISTENER:
                    int type = 1;
                    try {
                        if (args.get(0) != null) {
                            type = args.getInt(0);
                        }
                    } catch (JSONException e) {
                        CordovaPluginLog.e(TAG, "Error: ", e);
                    }

                    mNewLocationReceiver = new NewLocationReceiver(callbackContext, type);
                    mNewLocationIntentFilter = new IntentFilter(Res.BROADCAST_ACTION_ON_NEW_LOCATION);
                    LocalBroadcastManager.getInstance(mContext).
                        registerReceiver(mNewLocationReceiver, mNewLocationIntentFilter);
                    break;
                    default:
                    break;

                             /*
                    else if (action.equals(Res.ACTION_INTENT_STORE_PENDING_LOCATIONS) ||
                            action.equals(Res.ACTION_INTENT_RESTORE_PENDING_LOCATIONS)) {
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
     * request permissions if they are not granted by forwarding them to
     * the common plugin
     */
    private void requestLocationPermission() {
        for (String permission : LOCATION_PERMISSIONS) {
            CommonPlugin.addPermission(permission);
        }
    }

    private void init() {
        mExecutor.execute(mIndex.mRestoreAction);
    }


    public CordovaInterface getCordovaInterface() {
        return mCordovaInterface;
    }

    public void setCordovaInterface(CordovaInterface mCordovaInterface) {
        this.mCordovaInterface = mCordovaInterface;
    }
}
