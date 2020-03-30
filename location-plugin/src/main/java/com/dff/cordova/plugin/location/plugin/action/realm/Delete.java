package com.dff.cordova.plugin.location.plugin.action.realm;

import com.dff.cordova.plugin.location.core.helpers.realm.LocationRealmHelper;

import javax.inject.Inject;

/**
 * This RealmAction calls delete on the database.
 */
public class Delete extends RealmAction {
    public static final String ACTION = RealmAction.ACTION_PREFIX + "delete";
    
    private LocationRealmHelper locationRealmHelper;
    
    @Inject
    public Delete(LocationRealmHelper locationRealmHelper) {
        this.locationRealmHelper = locationRealmHelper;
        
        requiresPermissions = true;
    }
    
    @Override
    protected void execute() throws Exception {
        boolean result = locationRealmHelper.deleteRealm();
        callbackContext.success(result ? 1 : 0);
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
