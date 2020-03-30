package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.config.LocationConfigUpdateManager;
import com.dff.cordova.plugin.location.core.config.LocationUpdateRequest;
import com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest;

import org.json.JSONObject;

import javax.inject.Inject;

import static com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest.JSON_ARG_ID;

/**
 * Action to delete a specific location update request.
 */
public class DeleteLocationUpdateRequest extends LocationAction {
    public static final String ACTION = "deleteLocationUpdateRequest";
    
    private static final String[] JSON_ARGS = {
        JSON_ARG_ID
    };
    
    private LocationConfigUpdateManager locationConfigUpdateManager;
    private JsonLocationUpdateRequest jsonLocationUpdateRequest;
    
    @Inject
    public DeleteLocationUpdateRequest(
        LocationConfigUpdateManager locationConfigUpdateManager,
        JsonLocationUpdateRequest jsonLocationUpdateRequest
    ) {
        this.locationConfigUpdateManager = locationConfigUpdateManager;
        this.jsonLocationUpdateRequest = jsonLocationUpdateRequest;
        
        needsArgs = true;
        requiresPermissions = true;
    }
    
    @Override
    protected void execute() throws Exception {
        super.checkJsonArgs(JSON_ARGS);
        
        String id = jsonArgs.getString(JSON_ARG_ID);
        LocationUpdateRequest oldRequest =
            locationConfigUpdateManager.removeLocationUpdateRequest(id);
        JSONObject jsonObject = jsonLocationUpdateRequest.toJson(oldRequest);
        
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
