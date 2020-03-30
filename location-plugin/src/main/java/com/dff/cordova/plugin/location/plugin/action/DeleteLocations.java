package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.helpers.realm.GeoLocationRealmHelper;

import javax.inject.Inject;

/**
 * Action to delete locations filtered by jsonArgs.
 */
public class DeleteLocations extends LocationAction {
    public static final String ACTION = "deleteLocations";
    
    private GeoLocationRealmHelper geoLocationRealmHelper;
    
    @Inject
    public DeleteLocations(
        GeoLocationRealmHelper geoLocationRealmHelper
    ) {
        this.geoLocationRealmHelper = geoLocationRealmHelper;
        
        requiresPermissions = true;
    }
    
    @Override
    protected void execute() throws Exception {
        boolean success = geoLocationRealmHelper.delete(jsonArgs);
        callbackContext.success(success ? 1 : 0);
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
