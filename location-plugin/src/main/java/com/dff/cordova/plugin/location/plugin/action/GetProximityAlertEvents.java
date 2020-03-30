package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.helpers.realm.ProximityAlertEventRealmHelper;
import com.dff.cordova.plugin.location.core.json.realm.models.JsonProximityAlertEvent;
import com.dff.cordova.plugin.location.core.realm.models.ProximityAlertEvent;

import org.json.JSONArray;

import java.util.List;

import javax.inject.Inject;

/**
 * Returns a array of proximity alert events filtered by given parameters.
 */
public class GetProximityAlertEvents extends LocationAction {
    public static final String ACTION = "getProximityAlertEvents";
    
    private ProximityAlertEventRealmHelper proximityAlertEventRealmHelper;
    private JsonProximityAlertEvent jsonProximityAlertEvent;
    
    @Inject
    public GetProximityAlertEvents(
        ProximityAlertEventRealmHelper proximityAlertEventRealmHelper,
        JsonProximityAlertEvent jsonProximityAlertEvent
    ) {
        this.proximityAlertEventRealmHelper = proximityAlertEventRealmHelper;
        this.jsonProximityAlertEvent = jsonProximityAlertEvent;
        
        requiresPermissions = true;
    }
    
    @Override
    protected void execute() throws Exception {
        List<ProximityAlertEvent> alertEvents = proximityAlertEventRealmHelper.findAll(jsonArgs);
        JSONArray json = jsonProximityAlertEvent.toJson(alertEvents);
        
        callbackContext.success(json);
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
