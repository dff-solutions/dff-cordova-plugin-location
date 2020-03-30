package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.helpers.realm.ProximityAlertEventRealmHelper;

import javax.inject.Inject;

/**
 * Action to delete proximity alert events filtered by parameters.
 */
public class DeleteProximityAlertEvents extends LocationAction {
    public static final String ACTION = "deleteProximityAlertEvents";
    
    private ProximityAlertEventRealmHelper proximityAlertEventRealmHelper;
    
    @Inject
    public DeleteProximityAlertEvents(
        ProximityAlertEventRealmHelper proximityAlertEventRealmHelper
    ) {
        this.proximityAlertEventRealmHelper = proximityAlertEventRealmHelper;
        
        requiresPermissions = true;
    }
    
    @Override
    protected void execute() throws Exception {
        boolean success = proximityAlertEventRealmHelper.delete(jsonArgs);
        callbackContext.success(success ? 1 : 0);
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
