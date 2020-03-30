package com.dff.cordova.plugin.location.plugin.action.realm;

import com.dff.cordova.plugin.location.core.helpers.realm.LocationRealmHelper;

import javax.inject.Inject;

/**
 * This RealmAction executes the compact task on the database.
 */
public class Compact extends RealmAction {
    public static final String ACTION = RealmAction.ACTION_PREFIX + "compact";
    
    private LocationRealmHelper locationRealmHelper;
    
    @Inject
    public Compact(LocationRealmHelper locationRealmHelper) {
        this.locationRealmHelper = locationRealmHelper;
        
        requiresPermissions = true;
    }
    
    @Override
    public void execute() {
        boolean result = locationRealmHelper.compactRealm();
        callbackContext.success(result ? 1 : 0);
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
