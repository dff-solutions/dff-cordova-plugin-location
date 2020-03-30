package com.dff.cordova.plugin.location.plugin.action.realm;

import com.dff.cordova.plugin.location.core.classes.DatabaseInformation;
import com.dff.cordova.plugin.location.core.helpers.realm.GeoLocationRealmHelper;
import com.dff.cordova.plugin.location.core.helpers.realm.LocationRealmHelper;
import com.dff.cordova.plugin.location.core.helpers.realm.ProximityAlertEventRealmHelper;
import com.dff.cordova.plugin.location.core.json.classes.JsonDatabaseInformation;
import com.dff.cordova.plugin.shared.classes.json.JsonThrowable;
import com.dff.cordova.plugin.shared.helpers.PermissionHelper;
import com.dff.cordova.plugin.shared.log.Log;

import org.apache.cordova.CallbackContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.realm.RealmConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GetDatabaseInformationUnitTest {
    private final long DATABASE_SIZE = 1;
    private final int GLOBAL_COUNT = 2;
    private final int LOCAL_COUNT = 3;
    private final int LOCATION_COUNT = 4;
    private final int PROXIMITY_ALERT_COUNT = 5;
    
    @Mock
    Log log;
    
    @Mock
    CallbackContext callbackContext;
    
    @Mock
    JsonThrowable jsonThrowable;
    
    @Mock
    LocationRealmHelper locationRealmHelper;
    
    @Mock
    GeoLocationRealmHelper geoLocationRealmHelper;
    
    @Mock
    ProximityAlertEventRealmHelper proximityAlertEventRealmHelper;
    
    @Mock
    RealmConfiguration realmConfiguration;
    
    @Mock
    DatabaseInformation databaseInformation;
    
    @Mock
    JsonDatabaseInformation jsonDatabaseInformation;
    
    @Mock
    JSONObject jsonObject;
    
    @Mock
    PermissionHelper permissionHelper;
    
    @InjectMocks
    private GetDatabaseInformation actionInstance;
    
    private Class<? extends RealmAction> actionClass = GetDatabaseInformation.class;
    @BeforeEach
    void setup() {
        actionInstance.setLog(log);
        actionInstance.setAction(actionInstance.getActionName());
        actionInstance.setCallbackContext(callbackContext);
        actionInstance.setJsonThrowable(jsonThrowable);
        actionInstance.setPermissionHelper(permissionHelper);
    }
    
    @Test
    void actionName_shouldBeLowerClassName() {
        char[] c = actionClass.getSimpleName().toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        String classNameLower = new String(c);
        
        assertEquals(RealmAction.ACTION_PREFIX + classNameLower,
                     GetDatabaseInformation.ACTION);
        Assertions.assertEquals(RealmAction.ACTION_PREFIX + classNameLower,
                                actionInstance.getActionName());
    }
    
    @Test
    void shouldNotNeedArgs() {
        Assertions.assertFalse(actionInstance.isNeedsArgs());
    }
    
    @Test
    void shouldNeedPermissions() {
        Assertions.assertTrue(actionInstance.isRequiresPermissions());
    }
    
    @Test
    void shouldGetDatabaseInformation() throws JSONException {
        doReturn(true).when(permissionHelper).hasAllPermissions(any());
        
        doReturn(DATABASE_SIZE).when(locationRealmHelper).getDatabaseSizeInBytes();
        doReturn(GLOBAL_COUNT).when(locationRealmHelper).getGlobalInstanceCount();
        doReturn(LOCAL_COUNT).when(locationRealmHelper).getLocalInstanceCount();
        doReturn(realmConfiguration).when(locationRealmHelper).getRealmConfiguration();
        
        doReturn(LOCATION_COUNT).when(geoLocationRealmHelper).count(null);
        doReturn(PROXIMITY_ALERT_COUNT).when(proximityAlertEventRealmHelper).count(null);
        
        doReturn(jsonObject).when(jsonDatabaseInformation).toJson(databaseInformation);
        
        actionInstance.run();
        
        verify(databaseInformation).setDatabaseSizeInBytes(DATABASE_SIZE);
        verify(databaseInformation).setGlobalInstanceCount(GLOBAL_COUNT);
        verify(databaseInformation).setLocalInstanceCount(LOCAL_COUNT);
        verify(databaseInformation).setRealmConfiguration(realmConfiguration);
        verify(databaseInformation).setLocationCount(LOCATION_COUNT);
        verify(databaseInformation).setProximityAlertCount(PROXIMITY_ALERT_COUNT);
        
        verify(callbackContext).success(jsonObject);
    }
}

