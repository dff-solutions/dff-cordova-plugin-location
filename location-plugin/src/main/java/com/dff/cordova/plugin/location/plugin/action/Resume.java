package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.config.LocationConfigUpdateManager;

import javax.inject.Inject;

/**
 * Action to resume location updates and proximity alerts.
 */
public class Resume extends LocationAction {
    public static final String ACTION = "resume";
    
    private LocationConfigUpdateManager locationConfigUpdateManager;
    
    @Inject
    public Resume(LocationConfigUpdateManager locationConfigUpdateManager) {
        this.locationConfigUpdateManager = locationConfigUpdateManager;
    }
    
    @Override
    protected void execute() throws Exception {
        locationConfigUpdateManager.resume();
        callbackContext.success();
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
