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
 * Created by anahas on 23.12.2016.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 23.12.2016
 */
public class NewLocationReceiver extends BroadcastReceiver {

    private static final String TAG = "NewLocationReceiver";
    private CallbackContext mCallbackContext;

    public NewLocationReceiver(CallbackContext mCallbackContext) {
        this.mCallbackContext = mCallbackContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        Location lastGoodLocation = LocationResources.getLastGoodLocation();
        Log.d(TAG, "location = " + lastGoodLocation);
        if (lastGoodLocation != null) {
            AbstractPluginListener.sendPluginResult(mCallbackContext, LocationResources.getLastGoodLocationAsString());
        }
    }
}
