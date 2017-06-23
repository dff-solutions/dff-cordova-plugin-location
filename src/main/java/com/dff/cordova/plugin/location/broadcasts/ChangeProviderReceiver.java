package com.dff.cordova.plugin.location.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.dff.cordova.plugin.common.AbstractPluginListener;
import com.dff.cordova.plugin.location.resources.Res;

import org.apache.cordova.CallbackContext;

/**
 * Created by anahas on 05.05.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 05.05.2017
 */
public class ChangeProviderReceiver extends BroadcastReceiver {

    private static final String TAG = ChangeProviderReceiver.class.getSimpleName();

    private CallbackContext mCallbackContext;

    public ChangeProviderReceiver(CallbackContext mCallbackContext) {
        this.mCallbackContext = mCallbackContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

        if (intent.getAction().equals(Res.BROADCAST_ACTION_ON_CHANGED_PROVIDER)) {
            boolean isGpsProviderEnabled = intent.getBooleanExtra(Res.IS_PROVIDER_ENABLED, true);
            AbstractPluginListener.sendPluginResult(mCallbackContext, isGpsProviderEnabled);
        }
    }
}
