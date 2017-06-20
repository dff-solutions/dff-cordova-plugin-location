package com.dff.cordova.plugin.location.classes;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.*;
import android.util.Log;

import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.common.service.ServiceHandler;
import com.dff.cordova.plugin.location.abstracts.Action;
import com.dff.cordova.plugin.location.actions.RestoreAction;
import com.dff.cordova.plugin.location.broadcasts.StandStillReceiver;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.handlers.LocationRequestHandler;
import com.dff.cordova.plugin.location.interfaces.Executable;
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

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Class to execute incoming actions from JS.
 *
 * @author Anthony Nahas
 * @version 8.0.1
 * @since 15.12.2016
 */
@Singleton
@Module
public class Executor {

    private static final String TAG = "Executor";


    private Context mContext;
    private PreferencesHelper mPreferencesHelper;
    private DistanceSimulator mDistanceSimulator;

    //Actions
    private RestoreAction mRestoreAction;

    @Inject
    public Executor
        (@ApplicationContext Context mContext,
         PreferencesHelper mPreferencesHelper,
         DistanceSimulator mDistanceSimulator,
         RestoreAction mRestoreAction) {

        this.mContext = mContext;
        this.mPreferencesHelper = mPreferencesHelper;
        this.mDistanceSimulator = mDistanceSimulator;
    }


    public <T extends Action> Action execute(T action) {
        return action.execute();
    }

    /**
     *
     * <p>
     * //     * @param context - The context of the application
     */
//    public static void restore(Context context) {
//        context.startService(new Intent(context, PendingLocationsIntentService.class)
//                .setAction(LocationResources.ACTION_INTENT_RESTORE_PENDING_LOCATIONS));
//    }
    public void restore() {
        mContext.startService(new Intent(mContext, PendingLocationsIntentService.class)
            .setAction(LocationResources.ACTION_INTENT_RESTORE_PENDING_LOCATIONS));
    }

    /**
     * Stop the location service.
     */
    public void stopLocationService(HandlerThread handlerThread, ServiceHandler serviceHandler, CallbackContext callbackContext) {
//        Message msg = Message.obtain(null, LocationResources.WHAT.STOP_LOCATION_SERVICE.ordinal());
//        LocationRequestHandler handler = new LocationRequestHandler(handlerThread.getLooper(), mContext, callbackContext);
//        msg.replyTo = new Messenger(handler);
//        sendMessage(serviceHandler, msg, callbackContext);
//        mPreferencesHelper.setIsServiceStarted(false);
    }

    /**
     * Get the last good location from the service if it's available.
     * Good location means in this context: accuracy < 20m.
     *
     * @param callbackContext - The callback context used when calling back into JavaScript.
     * @param handlerThread   - The used handle thread
     * @param serviceHandler  - The used service handler.
     * @param args            - The exec() arguments.
     */
    public void getLocation(CallbackContext callbackContext,
                            HandlerThread handlerThread, ServiceHandler serviceHandler, JSONArray args) {
//        Message msg = Message.obtain(null, LocationResources.WHAT.GET_LOCATION.ordinal());
//        LocationRequestHandler handler = new LocationRequestHandler(handlerThread.getLooper(), mContext, callbackContext);
//        msg.replyTo = new Messenger(handler);
//        Bundle params = new Bundle();
//        params.putInt(LocationResources.LOCATION_RETURN_TYPE_KEY, args.optInt(0, LocationResources.LOCATION_RETURN_TYPE_INT));
//        msg.setData(params);
//        sendMessage(serviceHandler, msg, callbackContext);
    }

    /**
     * Get the all pending location that are stored in the location list.
     * These location has been stored to be sent back later.
     *
     * @param callbackContext - The callback context used when calling back into JavaScript.
     * @param args            - The exec() arguments.
     */
    public void getLocationList(CallbackContext callbackContext, JSONArray args) {
        Boolean canReset = true;
        try {
            JSONObject params = args.getJSONObject(0);
            if (params != null) {
                canReset = params.optBoolean(LocationResources.RESET, true);
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
                    if (canReset) {
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
                    if (canReset) {
                        LocationResources.clearJsonLocationsList();
                    }
                } else {
                    callbackContext.success(new JSONArray());
                    Log.d(TAG, "list < 0 ");
                }
            }
        }
    }


    public void getTotalDistance(CallbackContext callbackContext, JSONArray args) { //clean true - clear false
        boolean isClean = false;
        try {
            JSONObject params = args.getJSONObject(0);
            LocationResources.IS_TO_CALCULATE_DISTANCE = !params.optBoolean(LocationResources.RESET, false);
            if (!LocationResources.IS_TO_CALCULATE_DISTANCE) {
                LocationResources.STOP_ID = LocationResources.UNKNOWN;
            }
            isClean = params.optBoolean(LocationResources.CLEAN);
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        Multimap<String, Location> clonedMultimap = LocationResources.getLocationsMultimap();
        if (clonedMultimap == null) {
            callbackContext.error("Error: --> clonedMultimap may be null");
            return;
        }
        if (isClean) {
            clonedMultimap.removeAll(LocationResources.UNKNOWN);
        }
        ArrayList<Location> locationsList = new ArrayList<>(clonedMultimap.values());
        if (locationsList.size() > 1) {
            mDistanceSimulator.performDistanceCalculation(callbackContext, locationsList);
            if (!LocationResources.IS_TO_CALCULATE_DISTANCE) {
                LocationResources.clearLocationsMultimap();
            }
        } else {
            callbackContext.error("Error: --> locations list size = 0");
        }
    }

    public void handleStopId(String action, JSONArray args, CallbackContext callbackContext) {
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
                    if (params.optBoolean(LocationResources.RESET, false)) {
                        LocationResources.STOP_ID = "UNKNOWN";
                    }
                    ArrayList<Location> locationsList = new ArrayList<>(LocationResources.getLocationsMultimap().get(requestedStopID));
                    if (locationsList.isEmpty()) {
                        callbackContext.error(TAG + " : " + "Error -->  arraylist of stopid isEmpty - size = 0");
                        break;
                    }
                    mDistanceSimulator.performDistanceCalculation(callbackContext, locationsList);
                } catch (JSONException e) {
                    Log.e(TAG, "Error: ", e);
                    callbackContext.error("Error: " + e);
                }
                break;
            default:
                callbackContext.error("404 - action not found");
        }
    }

    public void getKeySetFromLocationsMultimap(CallbackContext callbackContext) {
        JSONArray jsonArray = new JSONArray(new ArrayList<>(LocationResources.getLocationsMultimap().keySet()));
        callbackContext.success(jsonArray);
    }

    /**
     * Send broadcast receiver to set the stop listener
     *
     * @param callbackContext - the used callbackcontext
     */
    public void setStopListener(CallbackContext callbackContext, JSONArray args) {
        LocationResources.STOP_HOLDER_COUNTER_LIMIT = args.optInt(0, LocationResources.STOP_HOLDER_COUNTER_LIMIT);
        LocationResources.STOP_HOLDER_MIN_DISTANCE = args.optInt(1, LocationResources.STOP_HOLDER_MIN_DISTANCE);
        LocationResources.STOP_HOLDER_DELAY = args.optInt(2, LocationResources.STOP_HOLDER_DELAY);
        mContext.registerReceiver(new StandStillReceiver(mContext, callbackContext), new IntentFilter(LocationResources.BROADCAST_ACTION_ON_STAND_STILL));
    }

    /**
     * Send broadcast receiver to stop the stop listener
     */
    public void stopStopListener() {
        mContext.sendBroadcast(new Intent(LocationResources.BROADCAST_ACTION_STOP));
    }

    private void sendMessage(ServiceHandler serviceHandler, Message msg, CallbackContext callbackContext) {
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
