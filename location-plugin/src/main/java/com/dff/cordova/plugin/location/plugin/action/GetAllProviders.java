package com.dff.cordova.plugin.location.plugin.action;

import android.location.LocationManager;

import com.dff.cordova.plugin.location.core.json.classes.JsonLocationProvider;
import com.dff.cordova.plugin.shared.helpers.JsonFactory;

import org.json.JSONArray;

import javax.inject.Inject;

/**
 * Action that returns all available location providers.
 */
public class GetAllProviders extends LocationAction {
    public static final String ACTION = "getAllProviders";
    
    private JsonLocationProvider jsonLocationProvider;
    private LocationManager locationManager;
    private JsonFactory jsonFactory;
    
    @Inject
    public GetAllProviders(
        JsonLocationProvider jsonLocationProvider,
        LocationManager locationManager,
        JsonFactory jsonFactory
    ) {
        this.jsonLocationProvider = jsonLocationProvider;
        this.locationManager = locationManager;
        this.jsonFactory = jsonFactory;
    }
    
    @Override
    protected void execute() throws Exception {
        JSONArray locationProviders = jsonFactory.getJSONArray();
        for (String provider : locationManager.getAllProviders()) {
            locationProviders.put(
                jsonLocationProvider.toJson(
                    locationManager.getProvider(provider)
                ));
        }
        
        if (locationProviders != null) {
            callbackContext.success(locationProviders);
        } else {
            callbackContext.success((String) null);
        }
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
