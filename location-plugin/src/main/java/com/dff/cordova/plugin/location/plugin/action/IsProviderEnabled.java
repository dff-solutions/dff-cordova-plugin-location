package com.dff.cordova.plugin.location.plugin.action;

import android.location.LocationManager;

import javax.inject.Inject;

import static com.dff.cordova.plugin.location.plugin.action.GetProvider.JSON_ARG_PROVIDER;

/**
 * Action that checks if the given provider is enabled.
 */
public class IsProviderEnabled extends LocationAction {
    public static final String ACTION = "isProviderEnabled";
    
    private static final String[] JSON_ARGS = {JSON_ARG_PROVIDER};

    private LocationManager locationManager;
    
    @Inject
    public IsProviderEnabled(LocationManager locationManager) {
        this.locationManager = locationManager;
        
        needsArgs = true;
    }
    
    @Override
    protected void execute() throws Exception {
        super.checkJsonArgs(JSON_ARGS);
        
        String provider = jsonArgs.getString(JSON_ARG_PROVIDER);
        
        boolean enabled = locationManager.isProviderEnabled(provider);
        
        callbackContext.success(enabled ? 1 : 0);
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
