package com.dff.cordova.plugin.location.events;

import org.apache.cordova.CallbackContext;

public abstract class OnCordovaAction {

    private CallbackContext mCallbackContext;

    public OnCordovaAction(CallbackContext callbackContext) {
        this.mCallbackContext = callbackContext;
    }

    public CallbackContext getCallbackContext() {
        return mCallbackContext;
    }
}
