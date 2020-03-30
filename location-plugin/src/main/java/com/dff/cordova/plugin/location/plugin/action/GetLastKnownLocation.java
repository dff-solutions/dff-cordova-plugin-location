package com.dff.cordova.plugin.location.plugin.action;

import android.location.Location;
import android.location.LocationManager;

import com.dff.cordova.plugin.location.core.json.realm.models.JsonGeoLocation;
import com.dff.cordova.plugin.location.core.realm.models.GeoLocation;

import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Provider;

import static com.dff.cordova.plugin.location.plugin.action.GetProvider.JSON_ARG_PROVIDER;

/**
 *  Returns a location indicating the data from the last known location fix
 *  obtained from the given provider.
 */
@SuppressWarnings("MissingPermission")
public class GetLastKnownLocation extends LocationAction {
    public static final String ACTION = "getLastKnownLocation";
    
    private static final String[] JSON_ARGS = {JSON_ARG_PROVIDER};
    
    private LocationManager locationManager;
    private final Provider<GeoLocation> geoLocationProvider;
    private final JsonGeoLocation jsonGeoLocation;

    @Inject
    public GetLastKnownLocation(
        LocationManager locationManager,
        Provider<GeoLocation> geoLocationProvider,
        JsonGeoLocation jsonGeoLocation
    ) {
        this.locationManager = locationManager;
        this.geoLocationProvider = geoLocationProvider;
        this.jsonGeoLocation = jsonGeoLocation;

        needsArgs = true;
        requiresPermissions = true;
    }
    
    @Override
    protected void execute() throws Exception {
        super.checkJsonArgs(JSON_ARGS);
        String provider = jsonArgs.getString(JSON_ARG_PROVIDER);

        JSONObject locationToJson = null;
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            GeoLocation geoLocation = geoLocationProvider.get();
            geoLocation.setLocation(location);
            geoLocation.setId(provider + "LastKnownLocation");
            locationToJson = jsonGeoLocation.toJson(geoLocation);
        }
        
        if (locationToJson != null) {
            callbackContext.success(locationToJson);
        } else {
            callbackContext.success((String) null);
        }
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
