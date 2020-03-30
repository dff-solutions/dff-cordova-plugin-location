package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.config.LocationConfigUpdateManager;

import javax.inject.Inject;

/**
 * Action to stop the foreground service.
 */
public class StopForeground extends LocationAction {
    public static final String ACTION = "stopForeground";
    
    private LocationConfigUpdateManager locationConfigUpdateManager;
    
    @Inject
    public StopForeground(LocationConfigUpdateManager locationConfigUpdateManager) {
        this.locationConfigUpdateManager = locationConfigUpdateManager;
    
        requiresPermissions = true;
    }
    
    @Override
    protected void execute() throws Exception {
        locationConfigUpdateManager.stopForeground();
        callbackContext.success();
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
