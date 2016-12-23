package com.dff.cordova.plugin.location;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.HandlerThread;
import android.os.Process;
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
 * @version 3.5.1
 * @since 28.11.2016
 */
public class LocationPlugin extends CommonServicePlugin {

    private static final String TAG = "LocationPlugin";

    private Context mContext;
    private HandlerThread mHandlerThread;
    private ServiceHandler mServiceHandler;
    private PreferencesHelper mPreferencesHelper;

    /**
     * Def-Constructor
     */
    public LocationPlugin() {
        super(TAG);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        mPreferencesHelper = new PreferencesHelper(mContext);
        mServiceHandler.bindService();
        mHandlerThread = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mContext.startService(new Intent(mContext, LocationService.class));

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
                    if (action.equals(LocationResources.ACTION_START_SERVICE)) {

                        Executor.startLocationService(mContext);

                    } else if (action.equals(LocationResources.ACTION_STOP_SERVICE)) {

                        Executor.stopLocationService(mContext);

                    } else if (action.equals(LocationResources.ACTION_GET_LOCATION)) {

                        Executor.getLocation(mContext, callbackContext, mHandlerThread, mServiceHandler, args);

                    } else if (action.equals(LocationResources.ACTION_GET_LOCATION_LIST)) {

                        Executor.getLocationList(callbackContext);

                    } else if (action.equals(LocationResources.ACTION_GET_TOTAL_DISTANCE)
                        || action.equals(LocationResources.ACTION_GET_CUSTOM_DISTANCE)
                        || action.equals(LocationResources.ACTION_RUN_TOTAL_DISTANCE_CALCULATOR)
                        || action.equals(LocationResources.ACTION_RUN_CUSTOM_DISTANCE_CALCULATOR)) {

                        Executor.sendActionToHandlerThread(mContext, callbackContext, mHandlerThread, mServiceHandler, action);

                    }
                    /*
                    else if (action.equals(LocationResources.ACTION_INTENT_STORE_PENDING_LOCATIONS) ||
                            action.equals(LocationResources.ACTION_INTENT_RESTORE_PENDING_LOCATIONS)) {
                        Intent pendingLocationsIntentService = new Intent(mContext, PendingLocationsIntentService.class);
                        pendingLocationsIntentService.setAction(action);
                        mContext.startService(pendingLocationsIntentService);
                    }
                    */
                    else if (action.equals(LocationResources.ACTION_SET_LOCATION_LISTENER)) {
                        mContext.registerReceiver(new NewLocationReceiver(callbackContext), new IntentFilter("test"));
                    } else try {
                        if (action.equals(LocationResources.ACTION_SET_MIN_ACCURACY) && args.get(0) != null) {
                            LocationResources.setLocationMinAccuracy(args.getInt(0));
                        } else if (action.equals(LocationResources.ACTION_SET_MAX_AGE) && args.get(0) != null) {
                            LocationResources.setLocationMaxAge(args.getInt(0));
                        }
                    } catch (JSONException e) {
                        CordovaPluginLog.e(TAG, "Error: ", e);
                    }
                }
            });
            return true;
        }
        return super.execute(action, args, callbackContext);
    }
}
