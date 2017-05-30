package com.dff.cordova.plugin.location.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import com.dff.cordova.plugin.common.AbstractPluginListener;
import com.dff.cordova.plugin.location.resources.LocationResources;
import org.apache.cordova.CallbackContext;

/**
 * Broadcast receiver that is responsible to forward the requested location to JS:
 *
 * @author Anthony Nahas
 * @version 1.4
 * @since 23.12.2016
 */
public class NewLocationReceiver extends BroadcastReceiver {

    private static final String TAG = "NewLocationReceiver";
    private CallbackContext mCallbackContext;
    private int mType = 1;

    /**
     * Custom constructor.
     *
     * @param mCallbackContext - The callbackcontext used to forward the location to JS.
     * @param mType            -
     */
    public NewLocationReceiver(CallbackContext mCallbackContext, int mType) {
        this.mCallbackContext = mCallbackContext;
        this.mType = mType;
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
        Location lastGoodLocation = LocationResources.getLastGoodLocation();
        Log.d(TAG, "location = " + lastGoodLocation);
        if (lastGoodLocation != null) {
            switch (mType) {
                case 0:
                    AbstractPluginListener.sendPluginResult(mCallbackContext, LocationResources.getLastGoodLocationAsString());
                    break;
                case 1:
                    AbstractPluginListener.sendPluginResult(mCallbackContext, LocationResources.getLastGoodLocationAsJson());
                    break;
            }

        }
    }
}
