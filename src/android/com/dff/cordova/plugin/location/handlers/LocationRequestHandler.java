package com.dff.cordova.plugin.location.handlers;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.utilities.PreferencesHelper;
import org.apache.cordova.CallbackContext;

/**
 * Created by anahas on 30.11.2016.
 *
 * @author Anthony Nahas
 * @version 0.9
 * @since 30.11.2016
 */
public class LocationRequestHandler extends Handler {

    private static final String TAG = "LocationRequestHandler";
    private CallbackContext mCallbackContext;
    private PreferencesHelper mPreferencesHelper;

    public LocationRequestHandler(Looper looper, Context context, CallbackContext mCallbackContext) {
        super(looper);
        mPreferencesHelper = new PreferencesHelper(context);
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
                    mCallbackContext.success("");
                }
                if (mPreferencesHelper.getCanLocationBeCleared()) {
                    LocationResources.clearLocationsList();
                }
                break;
            default:
                String errorMsg = "no what of msg has been found!";
                mCallbackContext.error(errorMsg);
                Log.w(TAG, errorMsg);
                break;
        }

        super.handleMessage(msg);
    }
}
