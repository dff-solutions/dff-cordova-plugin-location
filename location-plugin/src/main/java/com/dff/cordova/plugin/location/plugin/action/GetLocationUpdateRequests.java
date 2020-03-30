package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.config.LocationConfigHelper;
import com.dff.cordova.plugin.location.core.config.LocationUpdateRequest;
import com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest;

import org.json.JSONObject;

import java.util.Map;

import javax.inject.Inject;

/**
 * Action that returns a map of all known location update requests.
 */
public class GetLocationUpdateRequests extends LocationAction {
    public static final String ACTION = "getLocationUpdateRequests";
    
    private JsonLocationUpdateRequest jsonLocationUpdateRequest;
    private LocationConfigHelper locationConfigHelper;
    
    @Inject
    public GetLocationUpdateRequests(
        JsonLocationUpdateRequest jsonLocationUpdateRequest,
        LocationConfigHelper locationConfigHelper
    ) {
        this.jsonLocationUpdateRequest = jsonLocationUpdateRequest;
        this.locationConfigHelper = locationConfigHelper;
    }
    
    @Override
    protected void execute() throws Exception {
        Map<String, LocationUpdateRequest> locationUpdateRequests =
            locationConfigHelper.getLocationUpdateRequests();
        JSONObject json = jsonLocationUpdateRequest.toJson(locationUpdateRequests);
    
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
