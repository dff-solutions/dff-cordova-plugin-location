package com.dff.cordova.plugin.location.plugin.listeners.callback;

import com.dff.cordova.plugin.shared.log.Log;

import org.apache.cordova.CallbackContext;

/**
 * Abstract callback handler to store the callback.
 */
public class CallbackHandler {
    private static final String TAG = "AbstractCallbackHandler";
    CallbackContext callback;
    protected Log log;
    
    CallbackHandler(Log log) {
        this.log = log;
    }
    
    public void setCallback(CallbackContext callback) {
        this.callback = callback;
    }
    
    public void onDestroy() {
        log.d(TAG, "onDestroy");
        callback = null;
    }
}
