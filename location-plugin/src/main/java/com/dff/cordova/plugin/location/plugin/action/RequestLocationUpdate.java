package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.config.LocationConfigUpdateManager;
import com.dff.cordova.plugin.location.core.config.LocationUpdateRequest;
import com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest;

import javax.inject.Inject;

import static com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest.JSON_ARG_CRITERIA;
import static com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest.JSON_ARG_ID;
import static com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest.JSON_ARG_MIN_DISTANCE;
import static com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest.JSON_ARG_MIN_TIME;
import static com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest.JSON_ARG_PROVIDER;
import static com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest.JSON_ARG_SINGLE;

/**
 * Action to request location updates for given criteria or provider.
 * Provider or criteria must be set.
 */
public class RequestLocationUpdate extends LocationAction {
    public static final String ACTION = "requestLocationUpdate";
    
    private static final String[] JSON_ARGS = {
        JSON_ARG_ID,
        JSON_ARG_MIN_TIME,
        JSON_ARG_MIN_DISTANCE,
        JSON_ARG_SINGLE
    };
    
    private LocationConfigUpdateManager locationConfigUpdateManager;
    private JsonLocationUpdateRequest jsonLocationUpdateRequest;
    
    @Inject
    public RequestLocationUpdate(
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
        
        if (jsonArgs.isNull(JSON_ARG_PROVIDER) && jsonArgs.isNull(JSON_ARG_CRITERIA)) {
            throw new IllegalArgumentException("provider and criteria not set");
        }
        
        LocationUpdateRequest request = jsonLocationUpdateRequest.fromJson(jsonArgs);
        locationConfigUpdateManager.addLocationUpdateRequest(request);
        
        callbackContext.success(request.getId());
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
