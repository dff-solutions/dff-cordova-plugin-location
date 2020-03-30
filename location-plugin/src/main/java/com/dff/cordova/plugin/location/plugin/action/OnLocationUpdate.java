package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.plugin.listeners.callback.OnLocationUpdateCallbackHandler;

import javax.inject.Inject;

/**
 * Action to listen to location updates.
 */
public class OnLocationUpdate extends LocationAction {
    public static final String ACTION = "onLocationUpdate";
    
    private OnLocationUpdateCallbackHandler onLocationUpdateCallbackHandler;
    
    @Inject
    public OnLocationUpdate(
        OnLocationUpdateCallbackHandler onLocationUpdateCallbackHandler
    ) {
        this.onLocationUpdateCallbackHandler = onLocationUpdateCallbackHandler;
    }
    
    @Override
    protected void execute() throws Exception {
        onLocationUpdateCallbackHandler.setCallback(callbackContext);
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
