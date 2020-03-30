package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.plugin.listeners.callback.OnProximityAlertCallbackHandler;

import javax.inject.Inject;

/**
 * Action to listen to proximity alerts.
 */
public class OnProximityAlert extends LocationAction {
    public static final String ACTION = "onProximityAlert";
    
    private OnProximityAlertCallbackHandler onProximityAlertCallbackHandler;
    
    @Inject
    public OnProximityAlert(OnProximityAlertCallbackHandler onProximityAlertCallbackHandler) {
        this.onProximityAlertCallbackHandler = onProximityAlertCallbackHandler;
    }
    
    @Override
    protected void execute() throws Exception {
        onProximityAlertCallbackHandler.setCallback(callbackContext);
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
