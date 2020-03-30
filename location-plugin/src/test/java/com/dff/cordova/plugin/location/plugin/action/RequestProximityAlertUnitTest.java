package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.config.LocationConfigUpdateManager;
import com.dff.cordova.plugin.location.core.config.ProximityAlert;
import com.dff.cordova.plugin.location.core.json.classes.JsonProximityAlert;
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

import static com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest.JSON_ARG_ID;
import static com.dff.cordova.plugin.location.core.json.classes.JsonProximityAlert.JSON_ARG_EXPIRATION;
import static com.dff.cordova.plugin.location.core.json.classes.JsonProximityAlert.JSON_ARG_LATITUDE;
import static com.dff.cordova.plugin.location.core.json.classes.JsonProximityAlert.JSON_ARG_LONGITUDE;
import static com.dff.cordova.plugin.location.core.json.classes.JsonProximityAlert.JSON_ARG_RADIUS;
import static junit.framework.TestCase.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestProximityAlertUnitTest {
    @Mock
    private Log log;
    
    @Mock
    private CallbackContext callbackContext;
    
    @Mock
    private JsonThrowable jsonThrowable;
    
    @Mock
    private JSONArray args;
    
    @Mock
    private JSONObject jsonargs;
    
    @Mock
    private JSONObject jsonError;
    
    @Mock
    private ProximityAlert alert;
    
    @Mock
    private JsonProximityAlert jsonProximityAlert;
    
    @Mock
    private PermissionHelper permissionHelper;
    
    @Mock
    private LocationConfigUpdateManager locationConfigUpdateManager;
    
    @InjectMocks
    private RequestProximityAlert actionInstance;
    
    private Class<? extends LocationAction> actionClass = RequestProximityAlert.class;
    private final String id = "id";
    
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
        
        assertEquals(classNameLower, RequestProximityAlert.ACTION);
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
    void shouldRequestProximityAlert() throws JSONException {
        doReturn(true).when(permissionHelper).hasAllPermissions(any());
        // no args array
        assertNull(actionInstance.getArgs());
        when(jsonThrowable.toJson(any(Throwable.class))).thenReturn(jsonError);
        actionInstance.run();
        verify(callbackContext).error(jsonError);
        
        doReturn(jsonargs).when(args).optJSONObject(0);
        doReturn(true).when(jsonargs).has(JSON_ARG_ID);
        doReturn(true).when(jsonargs).has(JSON_ARG_LATITUDE);
        doReturn(true).when(jsonargs).has(JSON_ARG_LONGITUDE);
        doReturn(true).when(jsonargs).has(JSON_ARG_RADIUS);
        doReturn(true).when(jsonargs).has(JSON_ARG_EXPIRATION);
        
        doReturn(alert).when(jsonProximityAlert).fromJson(jsonargs);
        doReturn(id).when(alert).getId();
        
        actionInstance.setArgs(args);
        actionInstance.run();
        
        verify(jsonProximityAlert).fromJson(jsonargs);
        verify(locationConfigUpdateManager).addProximityAlert(alert);
        verify(callbackContext).success(id);
    }
}
