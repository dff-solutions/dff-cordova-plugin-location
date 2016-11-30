package com.dff.cordova.plugin.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.apache.cordova.CallbackContext;

/**
 * Created by anahas on 28.11.2016.
 */
public class LocationReceiver extends BroadcastReceiver {

    private CallbackContext mCallbackContext;

    public LocationReceiver(CallbackContext mCallbackContext) {
        this.mCallbackContext = mCallbackContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        //context.unregisterReceiver(...);
    }
}
