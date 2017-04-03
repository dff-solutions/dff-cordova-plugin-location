package com.dff.cordova.plugin.location.classes;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.*;
import android.util.Log;
import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.common.service.ServiceHandler;
import com.dff.cordova.plugin.location.broadcasts.StandStillReceiver;
import com.dff.cordova.plugin.location.handlers.LocationRequestHandler;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.services.LocationService;
import com.dff.cordova.plugin.location.services.PendingLocationsIntentService;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Class to execute incoming actions from JS.
 *
 * @author Anthony Nahas
 * @version 4.3.3
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
     * Start the location service.
     *
     * @param context - The context of the application.
     */
    public static void startLocationService(Context context, HandlerThread handlerThread, ServiceHandler serviceHandler,
                                            JSONArray args, CallbackContext callbackContext) {
        context.startService(new Intent(context, LocationService.class));
        Message msg = Message.obtain(null, LocationResources.WHAT.START_LOCATION_SERVICE.ordinal());
        LocationRequestHandler handler = new LocationRequestHandler(handlerThread.getLooper(), context, callbackContext);
        Bundle data = new Bundle();
        data.putLong(LocationResources.LOCATION_MIN_TIME_KEY, args.optLong(0, LocationResources.LOCATION_MIN_TIME));
        data.putFloat(LocationResources.LOCATION_MIN_DISTANCE_KEY, (float) args.optDouble(1, LocationResources.LOCATION_MIN_DISTANCE));
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
        params.putInt(LocationResources.LOCATION_RETURN_TYPE_KEY, args.optInt(0, LocationResources.LOCATION_RETURN_TYPE));
        msg.setData(params);
        sendMessage(serviceHandler, msg, callbackContext);
    }

    /**
     * Get the all pending location that are stored in the location list.
     * These location has been stored to be sent back later.
     *
     * @param callbackContext - The callback context used when calling back into JavaScript.
     */
    public static void getLocationList(CallbackContext callbackContext) {
        ArrayList<String> locationList = LocationResources.getLastGoodLocationListDffString();

        if (locationList.size() > 0) {
            callbackContext.success(new JSONArray(locationList));
            Log.d(TAG, "list > 0 ");
            //for (int i = 0; i < locationList.size() -1; i++) {
            //   Log.d(TAG, "loc " + i + " = " + locationList.get(i));
            //}
            LocationResources.clearLocationsList();
        } else {
            callbackContext.success(new JSONArray());
            Log.d(TAG, "list < 0 ");
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
        Message msg = Message.obtain(null, LocationResources.parseWHAT(action));
        LocationRequestHandler handler = new LocationRequestHandler(handlerThread.getLooper(),
            context, callbackContext);
        msg.replyTo = new Messenger(handler);
        sendMessage(serviceHandler, msg, callbackContext);
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
