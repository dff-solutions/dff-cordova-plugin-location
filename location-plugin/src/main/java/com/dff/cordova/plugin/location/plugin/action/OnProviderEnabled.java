package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.plugin.listeners.callback.OnProviderEnabledCallbackHandler;

import javax.inject.Inject;

/**
 * Action to listen to mode changes of the location providers.
 */
public class OnProviderEnabled extends LocationAction {
    public static final String ACTION = "onProviderEnabled";
    
    private OnProviderEnabledCallbackHandler onProviderEnabledCallbackHandler;
    
    @Inject
    public OnProviderEnabled(
        OnProviderEnabledCallbackHandler onProviderEnabledCallbackHandler
    ) {
        this.onProviderEnabledCallbackHandler = onProviderEnabledCallbackHandler;
    }
    
    @Override
    protected void execute() throws Exception {
        onProviderEnabledCallbackHandler.setCallback(callbackContext);
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
