package com.dff.cordova.plugin.location.handlers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.services.LocationService;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;
import org.apache.cordova.CallbackContext;
import org.json.JSONException;
import org.json.JSONObject;

import static com.dff.cordova.plugin.location.resources.LocationResources.CUSTOM_DISTANCE_CALCULATOR;

/**
 * Class to handle the answer sent from the location service handler.
 * On result, forward to the user (JS) using a callback context.
 *
 * @author Anthony Nahas
 * @version 4.1.0
 * @since 30.11.2016
 */
public class LocationRequestHandler extends Handler {

    private static final String TAG = "LocationRequestHandler";
    private Context mContext;
    private CallbackContext mCallbackContext;
    private PreferencesHelper mPreferencesHelper;

    /**
     * Custom constructor
     *
     * @param looper           - The used looper.
     * @param mContext         - The application/service context.
     * @param mCallbackContext - The callback context used to forward the result to the user.
     */
    public LocationRequestHandler(Looper looper, Context mContext, CallbackContext mCallbackContext) {
        super(looper);
        mPreferencesHelper = new PreferencesHelper(mContext);
        this.mContext = mContext;
        this.mCallbackContext = mCallbackContext;
    }

    /**
     * Handle the message sent by the location service handler.
     *
     * @param msg - The message received.
     */
    @Override
    public void handleMessage(Message msg) {
        Bundle data;
        LocationResources.WHAT msg_what = LocationResources.WHAT.values()[msg.what];

        switch (msg_what) {
            case START_LOCATION_SERVICE:
                data = msg.getData();
                if (data.getBoolean(LocationResources.IS_LOCATION_MANAGER_LISTENING)) {
                    mCallbackContext.success();
                } else {
                    mCallbackContext.error("No provider has been found to request a new location");
                }
                break;
            case STOP_LOCATION_SERVICE:
                mContext.stopService(new Intent(mContext, LocationService.class));
                break;
            case GET_LOCATION:
                Log.d(TAG, "what = " + msg.what);
                data = msg.getData();
                //String location = data.getString(LocationResources.DATA_LOCATION_KEY);
                int returnType = data.getInt(LocationResources.LOCATION_RETURN_TYPE_KEY);

                if (LocationResources.getLastGoodLocation() != null) {
                    switch (returnType) {
                        case 0:
                            mCallbackContext.success(LocationResources.getLastGoodLocationAsString());
                            break;
                        case 1:
                            mCallbackContext.success(LocationResources.getLastGoodLocationAsJson());
                            break;
                        default:
                            mCallbackContext.success(LocationResources.getLastGoodLocationAsJson());
                            break;
                    }
                } else {
                    mCallbackContext.success("");
                }
                if (mPreferencesHelper.getCanLocationBeCleared()) {
                    LocationResources.clearLocationsList();
                }
                break;
            case GET_TOTAL_DISTANCE_CALCULATOR:
                JSONObject totalDistance = new JSONObject();
                try {
                    totalDistance.put(LocationResources.JSON_KEY_DISTANCE, (double) LocationResources.TOTAL_DISTANCE_CALCULATOR.getDistance() / 1000);
                } catch (JSONException e) {
                    CordovaPluginLog.e(TAG, "Error: ", e);
                }
                mCallbackContext.success(totalDistance);
                LocationResources.TOTAL_DISTANCE_CALCULATOR.reset();
                break;
            case GET_CUSTOM_DISTANCE_CALCULATOR:
                JSONObject customDistance = new JSONObject();
                try {
                    customDistance.put(LocationResources.JSON_KEY_DISTANCE, (double) CUSTOM_DISTANCE_CALCULATOR.getDistance() / 1000);
                } catch (JSONException e) {
                    CordovaPluginLog.e(TAG, "Error: ", e);
                }
                mCallbackContext.success(customDistance);
                CUSTOM_DISTANCE_CALCULATOR.reset();
                break;
            case RUN_TOTAL_DISTANCE_CALCULATOR:
                mCallbackContext.success();
                break;
            case RUN_CUSTOM_DISTANCE_CALCULATOR:
                mCallbackContext.success();
                break;
            default:
                String errorMsg = "no 'what' property of the msg has been found!";
                //mCallbackContext.error(errorMsg);
                Log.w(TAG, errorMsg);
                break;
        }

        super.handleMessage(msg);
    }
}
