package com.dff.cordova.plugin.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.HandlerThread;
import android.os.Process;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.dff.cordova.plugin.common.CommonPlugin;
import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.common.service.CommonServicePlugin;
import com.dff.cordova.plugin.common.service.ServiceHandler;
import com.dff.cordova.plugin.location.broadcasts.NewLocationReceiver;
import com.dff.cordova.plugin.location.classes.Executor;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.services.LocationService;
import com.dff.cordova.plugin.location.utilities.helpers.FileHelper;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Cordova Plugin class that deals with the android's Location API, in order to get the location of the device as
 * well as to persist the locations when the app is not reachable.
 *
 * @author Anthony Nahas
 * @version 6.0.0
 * @since 28.11.2016
 */
public class LocationPlugin extends CommonServicePlugin {

    private static final String TAG = "LocationPlugin";
    private static final String[] LOCATION_PERMISSIONS =
        {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        };

    private Context mContext;
    private NewLocationReceiver mNewLocationReceiver;
    private IntentFilter mNewLocationIntentFilter;

    private static HandlerThread mHandlerThread;
    private static ServiceHandler mServiceHandler;

    /**
     * Def-Constructor
     */
    public LocationPlugin() {
        super(TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mNewLocationReceiver);
        FileHelper.storePendingLocation(mContext);
        FileHelper.storeLocationsMultimap(mContext);
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

    /**
     * Initialization of the plugin and the private properties.
     * and respectively bind and start the location service.
     */
    @Override
    public void pluginInitialize() {
        requestLocationPermission();
        mContext = cordova.getActivity().getApplicationContext();
        mContext.stopService(new Intent(mContext, LocationService.class));
        mServiceHandler = new ServiceHandler(this.cordova, LocationService.class);
        super.pluginInitialize(mServiceHandler);
        mServiceHandler.bindService();
        mHandlerThread = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        PreferencesHelper preferencesHelper = new PreferencesHelper(mContext);
        preferencesHelper.restoreProperties();
        preferencesHelper.setIsServiceStarted(false);
        //mContext.startService(new Intent(mContext, LocationService.class));
        Executor.restore(mContext);
        //new DistanceSimulator(mContext).simulateStaticJSON();
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
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Action = " + action);
                    switch (action) {
                        case LocationResources.ACTION_START_SERVICE:

                            Executor.startLocationService(mContext, mHandlerThread, mServiceHandler, args, callbackContext);

                            break;
                        case LocationResources.ACTION_STOP_SERVICE:

                            Executor.stopLocationService(mContext, mHandlerThread, mServiceHandler, callbackContext);

                            break;
                        case LocationResources.ACTION_GET_LOCATION:

                            Executor.getLocation(mContext, callbackContext, mHandlerThread, mServiceHandler, args);

                            break;
                        case LocationResources.ACTION_GET_LOCATION_LIST:

                            Executor.getLocationList(callbackContext, args);

                            break;

                        case LocationResources.ACTION_ENABLE_MAPPING_LOCATIONS:
                            LocationResources.IS_TO_CALCULATE_DISTANCE = true;
                            break;

                        case LocationResources.ACTION_GET_TOTAL_DISTANCE_CALCULATOR:
                            Executor.getTotalDistance(callbackContext, args);
                            break;

                        case LocationResources.ACTION_RUN_TOTAL_DISTANCE_CALCULATOR:
                        case LocationResources.ACTION_GET_CUSTOM_DISTANCE_CALCULATOR:
                        case LocationResources.ACTION_RUN_CUSTOM_DISTANCE_CALCULATOR:

                            Executor.sendActionToHandlerThread(mContext, callbackContext, mHandlerThread, mServiceHandler, action);

                            break;

                        case LocationResources.ACTION_SET_STOP_ID:
                        case LocationResources.ACTION_GET_LAST_STOP_ID:
                        case LocationResources.ACTION_CLEAR_STOP_ID:
                            Executor.handleStopId(action, args, callbackContext);
                            break;

                        case LocationResources.ACTION_GET_KEY_SET_FROM_LOCATIONS_MULTI_MAP:
                            Executor.getKeySetFromLocationsMultimap(callbackContext);
                            break;
                    /*
                    else if (action.equals(LocationResources.ACTION_INTENT_STORE_PENDING_LOCATIONS) ||
                            action.equals(LocationResources.ACTION_INTENT_RESTORE_PENDING_LOCATIONS)) {
                        Intent pendingLocationsIntentService = new Intent(mContext, PendingLocationsIntentService.class);
                        pendingLocationsIntentService.setAction(action);
                        mContext.startService(pendingLocationsIntentService);
                    }
                    */
                        case LocationResources.ACTION_SET_STOP_LISTENER:
                            Executor.setStopListener(mContext, callbackContext, args);
                            break;
                        case LocationResources.ACTION_CANCEL_STOP_LISTENER:
                            Executor.stopStopListener(mContext);
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
                            //mContext.registerReceiver(new NewLocationReceiver(callbackContext, type), new IntentFilter(LocationResources.BROADCAST_ACTION_ON_NEW_LOCATION));
                            break;
                        default:
                            try {
                                if (action.equals(LocationResources.ACTION_SET_MIN_ACCURACY) && args.get(0) != null) {
                                    LocationResources.setLocationMinAccuracy(args.getInt(0));
                                } else if (action.equals(LocationResources.ACTION_SET_MAX_AGE) && args.get(0) != null) {
                                    LocationResources.setLocationMaxAge(args.getInt(0));
                                } else if (action.equals(LocationResources.ACTION_SET_MIN_TIME) && args.get(0) != null) {
                                    LocationResources.setLocationMinTime(args.getLong(0)); //todo
                                }
                            } catch (JSONException e) {
                                CordovaPluginLog.e(TAG, "Error: ", e);
                            }
                            break;
                    }
                }
            });
            return true;
        }
        return false;
    }
}
