package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.config.LocationConfigUpdateManager;

import javax.inject.Inject;

/**
 * Action to start foreground service with notifcation to keep on receiving location
 * updates.
 */
public class StartForeground extends LocationAction {
    public static final String ACTION = "startForeground";
    
    private LocationConfigUpdateManager locationConfigUpdateManager;
    
    @Inject
    public StartForeground(LocationConfigUpdateManager locationConfigUpdateManager) {
        this.locationConfigUpdateManager = locationConfigUpdateManager;
    
        requiresPermissions = true;
    }
    
    @Override
    protected void execute() throws Exception {
        locationConfigUpdateManager.startForeground();
        callbackContext.success();
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
