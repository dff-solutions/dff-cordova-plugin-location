package com.dff.cordova.plugin.location.handlers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;
import org.apache.cordova.CallbackContext;

/**
 * Created by anahas on 30.11.2016.
 *
 *
 * @author Anthony Nahas
 * @since 30.11.2016
 * @version 0.9
 */
public class LocationRequestHandler extends Handler {

    private static final String TAG = "LocationRequestHandler";
    private CallbackContext mCallbackContext;

    public LocationRequestHandler(Looper looper, CallbackContext mCallbackContext) {
        super(looper);
        this.mCallbackContext = mCallbackContext;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case LocationResources.WHAT_GET_LOCATION:
                Log.d(TAG, "what = " + msg.what);
                Bundle data = msg.getData();
                String location = data.getString(LocationResources.DATA_LOCATION_KEY);
                if (location != null && location.length() > 0) {
                    Log.d(TAG, "Location = " + location);
                    mCallbackContext.success(location);
                } else {
                    String errorMsg = "Location is null or empty";
                    Log.w(TAG, errorMsg);
                    mCallbackContext.error(errorMsg);
                }
                break;
            default:
                Log.w(TAG, "no what of msg has been found!");
                break;
        }

        super.handleMessage(msg);
    }
}
