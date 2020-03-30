package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.config.LocationConfigHelper;
import com.dff.cordova.plugin.location.core.json.classes.JsonProximityAlert;

import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Action that returns a map of all known proximity alerts.
 */
public class GetProximityAlerts extends LocationAction {
    public static final String ACTION = "getProximityAlerts";
    
    private JsonProximityAlert jsonProximityAlert;
    private LocationConfigHelper locationConfigHelper;
    
    @Inject
    public GetProximityAlerts(
        JsonProximityAlert jsonProximityAlert,
        LocationConfigHelper locationConfigHelper
    ) {
        this.jsonProximityAlert = jsonProximityAlert;
        this.locationConfigHelper = locationConfigHelper;
    }
    
    @Override
    protected void execute() throws Exception {
        JSONObject json = jsonProximityAlert.toJson(locationConfigHelper.getProximityAlerts());
        
        if (json != null) {
            callbackContext.success(json);
        } else {
            callbackContext.success((String) null);
        }
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
