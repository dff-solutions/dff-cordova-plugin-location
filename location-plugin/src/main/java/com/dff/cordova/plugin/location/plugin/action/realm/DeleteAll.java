package com.dff.cordova.plugin.location.plugin.action.realm;

import com.dff.cordova.plugin.location.core.helpers.realm.LocationRealmHelper;

import javax.inject.Inject;

/**
 * This RealmAction calls deleteAll on the database.
 */
public class DeleteAll extends RealmAction {
    public static final String ACTION = RealmAction.ACTION_PREFIX + "deleteAll";
    
    private LocationRealmHelper locationRealmHelper;
    
    @Inject
    public DeleteAll(LocationRealmHelper locationRealmHelper) {
        this.locationRealmHelper = locationRealmHelper;
        
        requiresPermissions = true;
    }
    
    @Override
    protected void execute() throws Exception {
        locationRealmHelper.deleteAll();
        callbackContext.success();
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
