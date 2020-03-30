package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.config.LocationConfigUpdateManager;
import com.dff.cordova.plugin.location.core.config.ProximityAlert;
import com.dff.cordova.plugin.location.core.json.classes.JsonProximityAlert;

import javax.inject.Inject;

import static com.dff.cordova.plugin.location.core.json.classes.JsonProximityAlert.JSON_ARG_EXPIRATION;
import static com.dff.cordova.plugin.location.core.json.classes.JsonProximityAlert.JSON_ARG_ID;
import static com.dff.cordova.plugin.location.core.json.classes.JsonProximityAlert.JSON_ARG_LATITUDE;
import static com.dff.cordova.plugin.location.core.json.classes.JsonProximityAlert.JSON_ARG_LONGITUDE;
import static com.dff.cordova.plugin.location.core.json.classes.JsonProximityAlert.JSON_ARG_RADIUS;

/**
 * Action to request proximity alert for given arguments.
 */
public class RequestProximityAlert extends LocationAction {
    public static final String ACTION = "requestProximityAlert";
    
    private static final String[] JSON_ARGS = {
        JSON_ARG_ID,
        JSON_ARG_LATITUDE,
        JSON_ARG_LONGITUDE,
        JSON_ARG_RADIUS,
        JSON_ARG_EXPIRATION
    };
    
    private LocationConfigUpdateManager locationConfigUpdateManager;
    private JsonProximityAlert jsonProximityAlert;
    
    @Inject
    public RequestProximityAlert(
        LocationConfigUpdateManager locationConfigHelper,
        JsonProximityAlert jsonProximityAlert
    ) {
        this.locationConfigUpdateManager = locationConfigHelper;
        this.jsonProximityAlert = jsonProximityAlert;
        
        needsArgs = true;
        requiresPermissions = true;
    }
    
    @Override
    protected void execute() throws Exception {
        super.checkJsonArgs(JSON_ARGS);
        
        ProximityAlert alert = jsonProximityAlert.fromJson(jsonArgs);
        locationConfigUpdateManager.addProximityAlert(alert);
        
        callbackContext.success(alert.getId());
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
