package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.config.LocationConfig;
import com.dff.cordova.plugin.location.core.config.LocationConfigUpdateManager;
import com.dff.cordova.plugin.location.core.json.classes.JsonLocationConfig;

import javax.inject.Inject;

/**
 * Action to set config for location component. Without configuration no locations
 * are requested.
 */
public class SetConfig extends LocationAction {
    public static final String ACTION = "setConfig";
    
    private JsonLocationConfig jsonLocationConfig;
    private LocationConfigUpdateManager locationConfigUpdateManager;
    
    @Inject
    public SetConfig(
        JsonLocationConfig jsonLocationConfig,
        LocationConfigUpdateManager locationConfigUpdateManager
    ) {
        this.jsonLocationConfig = jsonLocationConfig;
        this.locationConfigUpdateManager = locationConfigUpdateManager;
    }
    
    @Override
    protected void execute() throws Exception {
        LocationConfig config = jsonLocationConfig.fromJson(jsonArgs);
        locationConfigUpdateManager.setConfig(config);
        
        callbackContext.success();
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
