package com.dff.cordova.plugin.location.handlers;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;
import org.apache.cordova.CallbackContext;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class to handle the answer sent from the location service handler.
 * On result, forward to the user (JS) using a callback context.
 *
 * @author Anthony Nahas
 * @version 2.0.0
 * @since 30.11.2016
 */
public class LocationRequestHandler extends Handler {

    private static final String TAG = "LocationRequestHandler";
    private CallbackContext mCallbackContext;
    private PreferencesHelper mPreferencesHelper;

    /**
     * Custom constructor
     *
     * @param looper           - The used looper.
     * @param context          - The application/service context.
     * @param mCallbackContext - The callback context used to forward the result to the user.
     */
    public LocationRequestHandler(Looper looper, Context context, CallbackContext mCallbackContext) {
        super(looper);
        mPreferencesHelper = new PreferencesHelper(context);
        this.mCallbackContext = mCallbackContext;
    }

    /**
     * Handle the message sent by the location service handler.
     *
     * @param msg - The message received.
     */
    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case LocationResources.WHAT_GET_LOCATION:
                Log.d(TAG, "what = " + msg.what);
                Bundle data = msg.getData();
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
            case LocationResources.WHAT_GET_DISTANCE_CALCULATOR_FULL:
                JSONObject distance = new JSONObject();
                try {
                    distance.put(LocationResources.JSON_KEY_TOTAL_DISTANCE, (double) LocationResources.getFullTotalDistance());
                } catch (JSONException e) {
                    Log.e(TAG, "Error: ", e);
                }
                mCallbackContext.success(distance);
            default:
                String errorMsg = "no 'what' property of the msg has been found!";
                mCallbackContext.error(errorMsg);
                Log.w(TAG, errorMsg);
                break;
        }

        super.handleMessage(msg);
    }
}
