package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.config.LocationConfigUpdateManager;
import com.dff.cordova.plugin.location.core.config.ProximityAlert;
import com.dff.cordova.plugin.location.core.json.classes.JsonProximityAlert;

import org.json.JSONObject;

import javax.inject.Inject;

import static com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest.JSON_ARG_ID;

/**
 * Action to delete a specific proximity alert.
 */
public class DeleteProximityAlert extends LocationAction {
    public static final String ACTION = "deleteProximityAlert";
    
    private static final String[] JSON_ARGS = {
        JSON_ARG_ID
    };
    
    private LocationConfigUpdateManager locationConfigUpdateManager;
    private JsonProximityAlert jsonProximityAlert;
    
    @Inject
    public DeleteProximityAlert(
        LocationConfigUpdateManager locationConfigUpdateManager,
        JsonProximityAlert jsonProximityAlert
    ) {
        this.locationConfigUpdateManager = locationConfigUpdateManager;
        this.jsonProximityAlert = jsonProximityAlert;
        
        needsArgs = true;
        requiresPermissions = true;
    }
    
    @Override
    protected void execute() throws Exception {
        super.checkJsonArgs(JSON_ARGS);
        
        String id = jsonArgs.getString(JSON_ARG_ID);
        ProximityAlert oldProximityAlert = locationConfigUpdateManager.removeProximityAlert(id);
        JSONObject jsonObject = jsonProximityAlert.toJson(oldProximityAlert);
    
        if (jsonObject != null) {
            callbackContext.success(jsonObject);
        } else {
            callbackContext.success((String) null);
        }
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
