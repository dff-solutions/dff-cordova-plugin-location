package com.dff.cordova.plugin.location.classes;

import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import com.dff.cordova.plugin.common.service.ServiceHandler;
import com.dff.cordova.plugin.location.handlers.LocationRequestHandler;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.services.LocationService;
import com.dff.cordova.plugin.location.services.PendingLocationsIntentService;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by anahas on 15.12.2016.
 *
 * @author Anthony Nahas
 * @version 1.1
 * @since 15.12.2016
 */
public class Executor {

    private static final String TAG = "Executor";

    public static void restore(Context context) {
        LocationResources.TOTAL_DISTANCE_CALCULATOR.restore(context, 0);
        LocationResources.CUSTOM_DISTANCE_CALCULATOR.restore(context, 1);
        context.startService(new Intent(context, PendingLocationsIntentService.class).setAction(LocationResources.ACTION_INTENT_RESTORE_PENDING_LOCATIONS));
    }

    public static void startLocationService(Context context) {
        context.startService(new Intent(context, LocationService.class));
    }

    public static void stopLocationService(Context context) {
        context.stopService(new Intent(context, LocationService.class));
    }

    public static void getLocation(Context context, CallbackContext callbackContext,
                                   HandlerThread handlerThread, ServiceHandler serviceHandler, JSONArray args) {
        Message msg = Message.obtain(null, LocationResources.WHAT_GET_LOCATION);
        LocationRequestHandler handler = new LocationRequestHandler(handlerThread.getLooper(), context, callbackContext);
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
            serviceHandler.getService().send(msg);
        } catch (RemoteException e) {
            Log.e(TAG, "Error: ", e);
        }
    }

    public static void getLocationList(CallbackContext callbackContext) {
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
    }

    public static void sendActionToHandlerThread(Context context, CallbackContext callbackContext,
                                                 HandlerThread handlerThread, ServiceHandler serviceHandler, String action) {
        Message msg = Message.obtain(null, LocationResources.parseWHAT(action));
        LocationRequestHandler handler = new LocationRequestHandler(handlerThread.getLooper(),
                context, callbackContext);
        msg.replyTo = new Messenger(handler);
        try {
            serviceHandler.getService().send(msg);
        } catch (RemoteException e) {
            Log.e(TAG, "Error: ", e);
            callbackContext.error("service not available");
        }
    }

}
