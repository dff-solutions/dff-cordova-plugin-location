package com.dff.cordova.plugin.location.plugin.action.realm;

import com.dff.cordova.plugin.location.core.classes.DatabaseInformation;
import com.dff.cordova.plugin.location.core.helpers.realm.GeoLocationRealmHelper;
import com.dff.cordova.plugin.location.core.helpers.realm.LocationRealmHelper;
import com.dff.cordova.plugin.location.core.helpers.realm.ProximityAlertEventRealmHelper;
import com.dff.cordova.plugin.location.core.json.classes.JsonDatabaseInformation;

import org.json.JSONObject;

import javax.inject.Inject;

/**
 * This RealmAction returns specific information of the database as json.
 */
public class GetDatabaseInformation extends RealmAction {
    public static final String ACTION = RealmAction.ACTION_PREFIX + "getDatabaseInformation";
    
    private LocationRealmHelper locationRealmHelper;
    private GeoLocationRealmHelper geoLocationRealmHelper;
    private ProximityAlertEventRealmHelper proximityAlertEventRealmHelper;
    private DatabaseInformation dbInfo;
    private JsonDatabaseInformation jsonDatabaseInformation;
    
    @Inject
    public GetDatabaseInformation(
        LocationRealmHelper locationRealmHelper,
        GeoLocationRealmHelper geoLocationRealmHelper,
        ProximityAlertEventRealmHelper proximityAlertEventRealmHelper,
        DatabaseInformation databaseInformation,
        JsonDatabaseInformation jsonDatabaseInformation
    ) {
        this.locationRealmHelper = locationRealmHelper;
        this.geoLocationRealmHelper = geoLocationRealmHelper;
        this.proximityAlertEventRealmHelper = proximityAlertEventRealmHelper;
        this.dbInfo = databaseInformation;
        this.jsonDatabaseInformation = jsonDatabaseInformation;
        
        requiresPermissions = true;
    }
    
    @Override
    protected void execute() throws Exception {
        dbInfo.setDatabaseSizeInBytes(locationRealmHelper.getDatabaseSizeInBytes());
        dbInfo.setGlobalInstanceCount(locationRealmHelper.getGlobalInstanceCount());
        dbInfo.setLocalInstanceCount(locationRealmHelper.getLocalInstanceCount());
        dbInfo.setRealmConfiguration(locationRealmHelper.getRealmConfiguration());
    
        dbInfo.setLocationCount(geoLocationRealmHelper.count(null));
        dbInfo.setProximityAlertCount(proximityAlertEventRealmHelper.count(null));
    
        JSONObject jsonObject = jsonDatabaseInformation.toJson(dbInfo);
        
        callbackContext.success(jsonObject);
    }
    
    @Override
    public String getActionName() {
        return ACTION;
    }
}
