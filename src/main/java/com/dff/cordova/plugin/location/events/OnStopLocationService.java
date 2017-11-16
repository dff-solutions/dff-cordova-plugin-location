package com.dff.cordova.plugin.location.events;

import org.apache.cordova.CallbackContext;

public class OnStopLocationService {
    private CallbackContext mCallbackContext;

    public OnStopLocationService(CallbackContext callbackContext) {
        this.mCallbackContext = callbackContext;
    }

    public CallbackContext getCallbackContext() {
        return mCallbackContext;
    }
}
