package com.dff.cordova.plugin.location;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.HandlerThread;
import android.os.Process;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.common.service.CommonServicePlugin;
import com.dff.cordova.plugin.common.service.ServiceHandler;
import com.dff.cordova.plugin.location.broadcasts.NewLocationReceiver;
import com.dff.cordova.plugin.location.classes.Executor;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.services.LocationService;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Cordova Plugin class that deals with the android'S Location API, in order to get the location of the device as
 * well as to persist the locations when the app is not reachable.
 *
 * @author Anthony Nahas
 * @version 4.5.5
 * @since 28.11.2016
 */
public class LocationPlugin extends CommonServicePlugin {

    private static final String TAG = "LocationPlugin";
    private static final String[] LOCATION_PERMISSIONS =
        {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        };

    private static final int LOCATION_PERMISSION_CODE = 0;


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


    /**
     * on start app --> check permissions.
     */
    @Override
    public void onStart() {
        super.onStart();
        if (!cordova.hasPermission(LOCATION_PERMISSIONS[0])) {
            getLocationPermission(LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mNewLocationReceiver);
    }

    /**
     * request permissions if they are not granted
     *
     * @param requestCode - the request code to handle the response of the request
     */
    private void getLocationPermission(int requestCode) {
        cordova.requestPermissions(this, requestCode, LOCATION_PERMISSIONS);
    }

    /**
     * If permissions are denied log and error and leave method
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @throws JSONException
     */
    public void onRequestPermissionResult(int requestCode, String[] permissions,
                                          int[] grantResults) throws JSONException {
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                CordovaPluginLog.e(TAG, "LOCATION PERMISSIONS DENIED");
                return;
            }
        }
    }

    /**
     * Initialization of the plugin and the private properties.
     * and respectively bind and start the location service.
     */
    @Override
    public void pluginInitialize() {
        mContext = cordova.getActivity().getApplicationContext();
        mServiceHandler = new ServiceHandler(this.cordova, LocationService.class);
        super.pluginInitialize(mServiceHandler);
        mServiceHandler.bindService();
        mHandlerThread = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        LocationResources.LOCATION_RETURN_TYPE = new PreferencesHelper(mContext).getReturnType();
        //mContext.startService(new Intent(mContext, LocationService.class));
        Executor.restore(mContext);
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

                            Executor.getLocationList(callbackContext);

                            break;
                        case LocationResources.ACTION_GET_TOTAL_DISTANCE:
                        case LocationResources.ACTION_GET_CUSTOM_DISTANCE:
                        case LocationResources.ACTION_RUN_TOTAL_DISTANCE_CALCULATOR:
                        case LocationResources.ACTION_RUN_CUSTOM_DISTANCE_CALCULATOR:

                            Executor.sendActionToHandlerThread(mContext, callbackContext, mHandlerThread, mServiceHandler, action);

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
        return super.execute(null, args, callbackContext);
    }

    public static HandlerThread getHandlerThread() {
        return mHandlerThread;
    }

    public static ServiceHandler getServiceHandler() {
        return mServiceHandler;
    }
}
