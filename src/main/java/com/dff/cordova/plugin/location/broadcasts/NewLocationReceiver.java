package com.dff.cordova.plugin.location.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.dff.cordova.plugin.common.AbstractPluginListener;
import com.dff.cordova.plugin.location.dagger.annotations.Shared;
import com.dff.cordova.plugin.location.resources.Res;
import com.dff.cordova.plugin.location.resources.Resources;

import org.apache.cordova.CallbackContext;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Broadcast receiver that is responsible to forward the requested location to JS:
 *
 * @author Anthony Nahas
 * @version 1.5
 * @since 23.12.2016
 */
@Singleton
public class NewLocationReceiver extends BroadcastReceiver {

    private static final String TAG = "BroadcastNewLocationReceiver";

    private Res mRes;
    private CallbackContext mCallbackContext;


    @Inject
    public NewLocationReceiver(@Shared Res mRes) {
        this.mRes = mRes;
    }

    /**
     * Receive a broadcast when a new location has been detected.
     *
     * @param context - The context of the application.
     * @param intent  - The intent that has been fired.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        JSONObject locationJSON = mRes.getLocationJSON();
        if (locationJSON != null) {
            AbstractPluginListener.sendPluginResult(mCallbackContext, locationJSON);
        }
    }

    public void setCallbackContext(CallbackContext mCallbackContext) {
        this.mCallbackContext = mCallbackContext;
    }
}
