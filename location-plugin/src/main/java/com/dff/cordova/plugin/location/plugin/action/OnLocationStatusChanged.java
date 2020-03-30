package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.plugin.listeners.callback.OnLocationStatusChangedCallbackHandler;

import javax.inject.Inject;

/**
 * Action to listen to location mode changes.
 */
public class OnLocationStatusChanged extends LocationAction {
    public static final String ACTION = "onLocationStatusChanged";
    
    private OnLocationStatusChangedCallbackHandler onLocationStatusChangedCallbackHandler;
    
    @Inject
    public OnLocationStatusChanged(
        OnLocationStatusChangedCallbackHandler onLocationStatusChangedCallbackHandler
    ) {
        this.onLocationStatusChangedCallbackHandler = onLocationStatusChangedCallbackHandler;
    }
    
    @Override
    protected void execute() throws Exception {
        onLocationStatusChangedCallbackHandler.setCallback(callbackContext);
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
