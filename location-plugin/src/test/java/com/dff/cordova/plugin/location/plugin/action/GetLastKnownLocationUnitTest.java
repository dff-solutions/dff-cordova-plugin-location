package com.dff.cordova.plugin.location.plugin.action;

import android.location.Location;
import android.location.LocationManager;

import com.dff.cordova.plugin.location.core.json.realm.models.JsonGeoLocation;
import com.dff.cordova.plugin.location.core.realm.models.GeoLocation;
import com.dff.cordova.plugin.shared.classes.json.JsonThrowable;
import com.dff.cordova.plugin.shared.helpers.PermissionHelper;
import com.dff.cordova.plugin.shared.log.Log;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Provider;

import static com.dff.cordova.plugin.location.plugin.action.GetProvider.JSON_ARG_PROVIDER;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetLastKnownLocationUnitTest {
    private final String providerName = "provider";
    
    @Mock
    private Log log;
    
    @Mock
    private CallbackContext callbackContext;
    
    @Mock
    private JSONArray args;
    
    @Mock
    private JSONObject jsonArgs;
    
    @Mock
    private JSONObject location2Json;
    
    @Mock
    private LocationManager locationManager;
    
    @Mock
    private JsonGeoLocation jsonGeoLocation;
    
    @Mock
    private JsonThrowable jsonThrowable;
    
    @Mock
    private Location location;
    
    @Mock
    private PermissionHelper permissionHelper;
    
    @Mock
    private JSONObject jsonError;

    @Mock
    private Provider<GeoLocation> geoLocationProvider;

    @Mock
    private GeoLocation geoLocation;
    
    @InjectMocks
    private GetLastKnownLocation actionInstance;
    
    private Class<? extends LocationAction> actionClass = GetLastKnownLocation.class;
    
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
        
        assertEquals(classNameLower, GetLastKnownLocation.ACTION);
        assertEquals(classNameLower, actionInstance.getActionName());
    }
    
    @Test
    void shouldNeedArgs() {
        assertTrue(actionInstance.isNeedsArgs());
    }
    
    @Test
    void shouldNeedPermissions() {
        assertTrue(actionInstance.isRequiresPermissions());
    }
    
    @Test
    void shouldGetLastKnownLocation() throws JSONException {
        doReturn(geoLocation).when(geoLocationProvider).get();

        // no args array
        assertNull(actionInstance.getArgs());
        when(jsonThrowable.toJson(any(Throwable.class))).thenReturn(jsonError);
        actionInstance.run();
        verify(callbackContext).error(jsonError);
        
        doReturn(jsonArgs).when(args).optJSONObject(0);
        doReturn(true).when(jsonArgs).has(JSON_ARG_PROVIDER);
        doReturn(providerName).when(jsonArgs).getString(JSON_ARG_PROVIDER);
        doReturn(true).when(permissionHelper).hasAllPermissions(any());
        actionInstance.setArgs(args);
        actionInstance.run();
        verify(callbackContext).success((String) isNull());

        doReturn(location).when(locationManager).getLastKnownLocation(providerName);
        doReturn(location2Json).when(jsonGeoLocation).toJson(geoLocation);
        
        actionInstance.run();
    
        verify(locationManager, times(2)).getLastKnownLocation(providerName);
        verify(geoLocation).setId(eq(providerName + "LastKnownLocation"));
        verify(jsonGeoLocation).toJson(geoLocation);
        verify(callbackContext).success(same(location2Json));
    }
    
}
