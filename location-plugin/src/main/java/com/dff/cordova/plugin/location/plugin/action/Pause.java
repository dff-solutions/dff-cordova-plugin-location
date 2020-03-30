package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.config.LocationConfigUpdateManager;

import javax.inject.Inject;

/**
 * Action to pause location updates and proximity alerts.
 */
public class Pause extends LocationAction {
    public static final String ACTION = "pause";
    
    private LocationConfigUpdateManager locationConfigUpdateManager;
    
    @Inject
    public Pause(LocationConfigUpdateManager locationConfigHelper) {
        this.locationConfigUpdateManager = locationConfigHelper;
    }
    
    @Override
    protected void execute() throws Exception {
        locationConfigUpdateManager.pause();
        callbackContext.success();
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
