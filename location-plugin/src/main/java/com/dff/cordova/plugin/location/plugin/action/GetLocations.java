package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.helpers.realm.GeoLocationRealmHelper;
import com.dff.cordova.plugin.location.core.json.realm.models.JsonGeoLocation;
import com.dff.cordova.plugin.location.core.realm.models.GeoLocation;

import org.json.JSONArray;

import java.util.List;

import javax.inject.Inject;

/**
 * Action that returns locations filtered by given arguments.
 */
public class GetLocations extends LocationAction {
    public static final String ACTION = "getLocations";

    private GeoLocationRealmHelper geoLocationRealmHelper;
    private JsonGeoLocation jsonGeoLocation;

    @Inject
    public GetLocations(
        GeoLocationRealmHelper geoLocationRealmHelper,
        JsonGeoLocation jsonGeoLocation
    ) {
        this.geoLocationRealmHelper = geoLocationRealmHelper;
        this.jsonGeoLocation = jsonGeoLocation;
    
        requiresPermissions = true;
    }

    @Override
    protected void execute() throws Exception {
        List<GeoLocation> locations = geoLocationRealmHelper.findAll(jsonArgs);
        JSONArray json = jsonGeoLocation.toJson(locations);

        callbackContext.success(json);
    }

    @Override
    public String getActionName() {
        return ACTION;
    }
}
