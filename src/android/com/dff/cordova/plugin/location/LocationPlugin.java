package com.dff.cordova.plugin.location;

import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.util.Log;
import com.dff.cordova.plugin.common.service.CommonServicePlugin;
import com.dff.cordova.plugin.common.service.ServiceHandler;
import com.dff.cordova.plugin.location.handlers.LocationRequestHandler;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.services.LocationService;
import com.dff.cordova.plugin.location.services.PendingLocationsIntentService;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Cordova Plugin class that deals with the android'S Location API, in order to get the location of the device as
 * well as to persist the locations when the app is not reachable.
 *
 * @author Anthony Nahas
 * @version 2.1.2
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
        mPreferencesHelper = new PreferencesHelper(cordova.getActivity().getApplicationContext());
        mServiceHandler.bindService();
        mHandlerThread = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mContext.startService(new Intent(mContext, LocationService.class));
        mContext.startService(new Intent(mContext, PendingLocationsIntentService.class).setAction(LocationResources.ACTION_INTENT_RESTORE_PENDING_LOCATIONS));
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
                        mContext.startService(new Intent(mContext, LocationService.class));
                    } else if (action.equals(LocationResources.ACTION_STOP_SERVICE)) {
                        mContext.stopService(new Intent(mContext, LocationService.class));
                    } else if (action.equals(LocationResources.ACTION_GET_LOCATION)) {
                        Message msg = Message.obtain(null, LocationResources.WHAT_GET_LOCATION);
                        LocationRequestHandler handler = new LocationRequestHandler(mHandlerThread.getLooper(), cordova.getActivity().getApplicationContext(), callbackContext);
                        msg.replyTo = new Messenger(handler);
                        Bundle params = new Bundle();
                        try {
                            if (args.get(0) != null) {
                                params.putInt(LocationResources.LOCATION_RETURN_TYPE_KEY, args.getInt(0));
                            } else {
                                params.putInt(LocationResources.LOCATION_RETURN_TYPE_KEY, LocationResources.LOCATION_RETURN_TYPE);
                            }
                            msg.setData(params);
                        } catch (JSONException e) {
                            Log.e(TAG, "Error: ", e);
                        }
                        try {
                            mServiceHandler.getService().send(msg);
                        } catch (RemoteException e) {
                            Log.e(TAG, "Error: ", e);
                        }
                    } else if (action.equals(LocationResources.ACTION_GET_LOCATION_LIST)) {
                        ArrayList<String> locationList = LocationResources.getLastGoodLocationList();

                        if (locationList.size() > 0) {
                            callbackContext.success(new JSONArray(locationList));
                            Log.d(TAG, "list > 0 ");
                            for (int i = 0; i < locationList.size(); i++) {
                                Log.d(TAG, "loc " + i + " = " + locationList.get(i));
                            }
                            LocationResources.clearLocationsList();
                        } else {
                            callbackContext.success();
                            Log.d(TAG, "list < 0 ");
                        }
                    } else if (action.equals(LocationResources.ACTION_GET_FULL_DISTANCE)) {
                        Message msg = Message.obtain(null, LocationResources.WHAT_GET_DISTANCE_CALCULATOR_FULL);
                        LocationRequestHandler handler = new LocationRequestHandler(mHandlerThread.getLooper(),
                                cordova.getActivity().getApplicationContext(), callbackContext);
                        msg.replyTo = new Messenger(handler);
                        try {
                            mServiceHandler.getService().send(msg);
                        } catch (RemoteException e) {
                            Log.e(TAG, "Error: ", e);
                        }
                    } else if (action.equals(LocationResources.ACTION_INTENT_STORE_PENDING_LOCATIONS) ||
                            action.equals(LocationResources.ACTION_INTENT_RESTORE_PENDING_LOCATIONS)) {
                        Intent pendingLocationsIntentService = new Intent(mContext, PendingLocationsIntentService.class);
                        pendingLocationsIntentService.setAction(action);
                        mContext.startService(pendingLocationsIntentService);
                    } else try {
                        if (action.equals(LocationResources.ACTION_SET_MIN_ACCURACY) && args.get(0) != null) {
                            LocationResources.setLocationMinAccuracy(args.getInt(0));
                        } else if (action.equals(LocationResources.ACTION_SET_MAX_AGE) && args.get(0) != null) {
                            LocationResources.setLocationMaxAge(args.getInt(0));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return true;
        }
        return super.execute(action, args, callbackContext);
    }
}
