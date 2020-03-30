package com.dff.cordova.plugin.location.plugin.action;

import android.location.LocationManager;

import com.dff.cordova.plugin.location.core.json.classes.JsonLocationProvider;

import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Action that returns a provider based on given provider name.
 */
public class GetProvider extends LocationAction {
    public static final String ACTION = "getProvider";
    public static final String JSON_ARG_PROVIDER = "provider";
    
    private static final String[] JSON_ARGS = {JSON_ARG_PROVIDER};
    
    private JsonLocationProvider jsonLocationProvider;
    private LocationManager locationManager;
    
    @Inject
    public GetProvider(
        JsonLocationProvider jsonLocationProvider,
        LocationManager locationManager
    ) {
        this.jsonLocationProvider = jsonLocationProvider;
        this.locationManager = locationManager;
        
        needsArgs = true;
    }
    
    @Override
    protected void execute() throws Exception {
        
        super.checkJsonArgs(JSON_ARGS);
        
        String provider = jsonArgs.getString(JSON_ARG_PROVIDER);
        
        JSONObject locationProvider =
            jsonLocationProvider.toJson(locationManager.getProvider(provider));
    
        if (locationProvider != null) {
            callbackContext.success(locationProvider);
        } else {
            callbackContext.success((String) null);
        }
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
