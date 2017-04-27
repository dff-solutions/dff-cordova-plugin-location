package com.dff.cordova.plugin.location.classes;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.*;
import android.util.Log;
import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.common.service.ServiceHandler;
import com.dff.cordova.plugin.location.broadcasts.StandStillReceiver;
import com.dff.cordova.plugin.location.handlers.LocationRequestHandler;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.services.LocationService;
import com.dff.cordova.plugin.location.services.PendingLocationsIntentService;
import com.dff.cordova.plugin.location.simulators.DistanceSimulator;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;
import com.google.common.collect.Multimap;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Class to execute incoming actions from JS.
 *
 * @author Anthony Nahas
 * @version 6.0.0
 * @since 15.12.2016
 */
public class Executor {

    private static final String TAG = "Executor";

    /**
     * Restore stored value from the shared preference or respectively from file system.
     *
     * @param context - The context of the application
     */
    public static void restore(Context context) {
        LocationResources.TOTAL_DISTANCE_CALCULATOR.restore(context, 0);
        LocationResources.CUSTOM_DISTANCE_CALCULATOR.restore(context, 1);
        context.startService(new Intent(context, PendingLocationsIntentService.class)
            .setAction(LocationResources.ACTION_INTENT_RESTORE_PENDING_LOCATIONS));
    }

    /**
     * Start the location service with params.
     *
     * @param context - The context of the application.
     */
    public static void startLocationService(Context context, HandlerThread handlerThread, ServiceHandler serviceHandler,
                                            JSONArray args, CallbackContext callbackContext) {
        context.startService(new Intent(context, LocationService.class));
        Message msg = Message.obtain(null, LocationResources.WHAT.START_LOCATION_SERVICE.ordinal());
        LocationRequestHandler handler = new LocationRequestHandler(handlerThread.getLooper(), context, callbackContext);
        Bundle data = new Bundle();
        try {
            JSONObject params = args.getJSONObject(0);
            if (params != null) {
                LocationResources.LOCATION_RETURN_TYPE = params.optString(LocationResources.RETURN_TYPE, LocationResources.LOCATION_RETURN_TYPE);
                LocationResources.LOCATION_MIN_TIME = params.optLong(LocationResources.MIN_TIME, LocationResources.LOCATION_MIN_TIME);
                LocationResources.LOCATION_MIN_DISTANCE = (float) params.optDouble(LocationResources.MIN_DISTANCE, LocationResources.LOCATION_MIN_DISTANCE);
                LocationResources.LOCATION_MIN_ACCURACY = params.optInt(LocationResources.MIN_ACCURACY, LocationResources.LOCATION_MIN_ACCURACY);
                LocationResources.LOCATION_MAX_AGE = params.optInt(LocationResources.MAX_AGE, LocationResources.LOCATION_MAX_AGE);
                LocationResources.LOCATION_DELAY = params.optInt(LocationResources.DELAY, LocationResources.LOCATION_DELAY);
                new PreferencesHelper(context).storeProperties();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        data.putLong(LocationResources.LOCATION_MIN_TIME_KEY, LocationResources.LOCATION_MIN_TIME);
        data.putFloat(LocationResources.LOCATION_MIN_DISTANCE_KEY, LocationResources.LOCATION_MIN_DISTANCE);
        msg.setData(data);
        msg.replyTo = new Messenger(handler);
        sendMessage(serviceHandler, msg, callbackContext);
    }

    /**
     * Stop the location service.
     *
     * @param context - The context of the application.
     */
    public static void stopLocationService(Context context, HandlerThread handlerThread, ServiceHandler serviceHandler, CallbackContext callbackContext) {
        Message msg = Message.obtain(null, LocationResources.WHAT.STOP_LOCATION_SERVICE.ordinal());
        LocationRequestHandler handler = new LocationRequestHandler(handlerThread.getLooper(), context, callbackContext);
        msg.replyTo = new Messenger(handler);
        sendMessage(serviceHandler, msg, callbackContext);
        new PreferencesHelper(context).setIsServiceStarted(false);
    }

    /**
     * Get the last good location from the service if it's available.
     * Good location means in this context: accuracy < 20m.
     *
     * @param context         - The context of the application.
     * @param callbackContext - The callback context used when calling back into JavaScript.
     * @param handlerThread   - The used handle thread
     * @param serviceHandler  - The used service handler.
     * @param args            - The exec() arguments.
     */
    public static void getLocation(Context context, CallbackContext callbackContext,
                                   HandlerThread handlerThread, ServiceHandler serviceHandler, JSONArray args) {
        Message msg = Message.obtain(null, LocationResources.WHAT.GET_LOCATION.ordinal());
        LocationRequestHandler handler = new LocationRequestHandler(handlerThread.getLooper(), context, callbackContext);
        msg.replyTo = new Messenger(handler);
        Bundle params = new Bundle();
        params.putInt(LocationResources.LOCATION_RETURN_TYPE_KEY, args.optInt(0, LocationResources.LOCATION_RETURN_TYPE_INT));
        msg.setData(params);
        sendMessage(serviceHandler, msg, callbackContext);
    }

    /**
     * Get the all pending location that are stored in the location list.
     * These location has been stored to be sent back later.
     *
     * @param callbackContext - The callback context used when calling back into JavaScript.
     * @param args            - The exec() arguments.
     */
    public static void getLocationList(CallbackContext callbackContext, JSONArray args) {
        Boolean canBeCleared = true;
        try {
            JSONObject params = args.getJSONObject(0);
            if (params != null) {
                canBeCleared = params.optBoolean(LocationResources.CLEAR, true);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        switch (LocationResources.LOCATION_RETURN_TYPE) {
            case LocationResources.DFF_STRING:
                ArrayList<String> dffStringLocationList = LocationResources.getLocationListDffString();

                if (dffStringLocationList.size() > 0) {
                    callbackContext.success(new JSONArray(dffStringLocationList));
                    Log.d(TAG, "list > 0 ");
                    if (canBeCleared) {
                        LocationResources.clearDffStringLocationsList();
                    }
                } else {
                    callbackContext.success(new JSONArray());
                    Log.d(TAG, "list < 0 ");
                }
                break;
            case LocationResources.JSON: {
                ArrayList<JSONObject> jsonLocationList = LocationResources.getLocationListJson();

                if (jsonLocationList.size() > 0) {
                    callbackContext.success(new JSONArray(jsonLocationList));
                    Log.d(TAG, "list > 0 ");
                    if (canBeCleared) {
                        LocationResources.clearJsonLocationsList();
                    }
                } else {
                    callbackContext.success(new JSONArray());
                    Log.d(TAG, "list < 0 ");
                }
            }
        }
    }

    /**
     * Forward the action to the handler thread.
     *
     * @param context-         The context of the application.
     * @param callbackContext- The callback context used when calling back into JavaScript.
     * @param handlerThread-   The used handle thread
     * @param serviceHandler-  The used service handler.
     * @param action           The action to execute.
     */
    public static void sendActionToHandlerThread(Context context, CallbackContext callbackContext,
                                                 HandlerThread handlerThread, ServiceHandler serviceHandler, String action) {
        try {
            String[] what_action_filtered = action.split(Pattern.quote("."));
            Message msg = Message.obtain(null, LocationResources.WHAT.valueOf(what_action_filtered[2]).ordinal());
            LocationRequestHandler handler = new LocationRequestHandler(handlerThread.getLooper(),
                context, callbackContext);
            msg.replyTo = new Messenger(handler);
            sendMessage(serviceHandler, msg, callbackContext);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Error: ", e);
        }
    }

    public static void getTotalDistance(CallbackContext callbackContext, JSONArray args) { //clean true - clear false
        boolean isClean = false;
        try {
            JSONObject params = args.getJSONObject(0);
            LocationResources.IS_TO_CALCULATE_DISTANCE = params.optBoolean(LocationResources.CLEAR, false);
            if (!LocationResources.IS_TO_CALCULATE_DISTANCE) {
                LocationResources.STOP_ID = LocationResources.UNKNOWN;
            }
            isClean = params.getBoolean(LocationResources.CLEAN);
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        Multimap<String, Location> clonedMultimap = LocationResources.getLocationsMultimap();
        if (isClean) {
            assert clonedMultimap != null;
            clonedMultimap.removeAll(LocationResources.UNKNOWN);
        }
        ArrayList<Location> locationsList = new ArrayList<>(clonedMultimap.values());
        if (locationsList.size() > 0) {
            new DistanceSimulator().performDistanceCalculation(callbackContext, locationsList);
            if (!LocationResources.IS_TO_CALCULATE_DISTANCE) {
                LocationResources.clearLocationsMultimap();
            }
        } else {
            callbackContext.error("Error: --> locations list size = 0");
        }
    }

    public static void handleStopId(String action, JSONArray args, CallbackContext callbackContext) {
        switch (action) {
            case LocationResources.ACTION_SET_STOP_ID:
                try {
                    JSONObject params = args.getJSONObject(0);
                    LocationResources.STOP_ID = params.optString(LocationResources.JSON_KEY_STOP_ID, LocationResources.STOP_ID);
                    callbackContext.success();
                } catch (JSONException e) {
                    Log.e(TAG, "Error: ", e);
                    callbackContext.error("Error: " + e);
                }
                break;

            case LocationResources.ACTION_GET_LAST_STOP_ID:
                callbackContext.success(LocationResources.STOP_ID);
                break;

            case LocationResources.ACTION_CLEAR_STOP_ID:
                try {
                    JSONObject params = args.getJSONObject(0);
                    String requestedStopID = params.optString(LocationResources.JSON_KEY_STOP_ID, LocationResources.STOP_ID);
                    if (params.optBoolean(LocationResources.CLEAR, false)) {
                        LocationResources.STOP_ID = "UNKNOWN";
                    }
                    ArrayList<Location> locationsList = new ArrayList<>(LocationResources.getLocationsMultimap().get(requestedStopID));
                    new DistanceSimulator().performDistanceCalculation(callbackContext, locationsList);
                } catch (JSONException e) {
                    Log.e(TAG, "Error: ", e);
                    callbackContext.error("Error: " + e);
                }
                break;
            default:
                callbackContext.error("404 - action not found");
        }
    }

    public static void getKeySetFromLocationsMultimap(CallbackContext callbackContext) {
        JSONArray jsonArray = new JSONArray(new ArrayList<>(LocationResources.getLocationsMultimap().keySet()));
        callbackContext.success(jsonArray);
    }

    /**
     * Send broadcast receiver to set the stop listener
     *
     * @param context         - the used context
     * @param callbackContext - the used callbackcontext
     */
    public static void setStopListener(Context context, CallbackContext callbackContext, JSONArray args) {
        LocationResources.STOP_HOLDER_COUNTER_LIMIT = args.optInt(0, LocationResources.STOP_HOLDER_COUNTER_LIMIT);
        LocationResources.STOP_HOLDER_MIN_DISTANCE = args.optInt(1, LocationResources.STOP_HOLDER_MIN_DISTANCE);
        LocationResources.STOP_HOLDER_DELAY = args.optInt(2, LocationResources.STOP_HOLDER_DELAY);
        context.registerReceiver(new StandStillReceiver(context, callbackContext), new IntentFilter(LocationResources.BROADCAST_ACTION_ON_STAND_STILL));
    }

    /**
     * Send broadcast receiver to stop the stop listener
     *
     * @param context - the used context
     */
    public static void stopStopListener(Context context) {
        context.sendBroadcast(new Intent(LocationResources.BROADCAST_ACTION_STOP));
    }

    private static void sendMessage(ServiceHandler serviceHandler, Message msg, CallbackContext callbackContext) {
        try {
            Messenger messenger = serviceHandler.getService();
            if (messenger != null) {
                messenger.send(msg);
            }
        } catch (RemoteException | NullPointerException e) {
            CordovaPluginLog.e(TAG, "Error: ", e);
            callbackContext.error("Error while sending a message within the location service: " + e);
        }
    }

}
