package com.dff.cordova.plugin.location.handlers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.dff.cordova.plugin.location.resources.LocationResources;
import org.apache.cordova.CallbackContext;

/**
 * Created by anahas on 30.11.2016.
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

        switch (msg.what){
            case LocationResources.ACTION_GET_LOCATION:

        }

        super.handleMessage(msg);
    }
}
