package com.dff.cordova.plugin.location.events;

import org.apache.cordova.CallbackContext;

public class OnStartLocationService {

    private CallbackContext mCallbackContext;

    public OnStartLocationService(CallbackContext callbackContext) {
        this.mCallbackContext = callbackContext;
    }

    public CallbackContext getCallbackContext() {
        return mCallbackContext;
    }
}
