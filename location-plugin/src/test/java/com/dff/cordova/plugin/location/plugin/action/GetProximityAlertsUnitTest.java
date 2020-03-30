package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.config.LocationConfigHelper;
import com.dff.cordova.plugin.location.core.json.classes.JsonProximityAlert;
import com.dff.cordova.plugin.shared.classes.json.JsonThrowable;
import com.dff.cordova.plugin.shared.helpers.PermissionHelper;
import com.dff.cordova.plugin.shared.log.Log;

import org.apache.cordova.CallbackContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GetProximityAlertsUnitTest {
    @Mock
    private Log log;
    
    @Mock
    private CallbackContext callbackContext;
    
    @Mock
    private JsonThrowable jsonThrowable;
    
    @Mock
    private JSONObject requests;
    
    @Mock
    private PermissionHelper permissionHelper;
    
    @Mock
    private LocationConfigHelper locationConfigHelper;
    
    @Mock
    private JsonProximityAlert jsonProximityAlert;
    
    @InjectMocks
    private GetProximityAlerts actionInstance;
    
    private Class<? extends LocationAction> actionClass = GetProximityAlerts.class;
    
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
        
        assertEquals(classNameLower, GetProximityAlerts.ACTION);
        assertEquals(classNameLower, actionInstance.getActionName());
    }
    
    @Test
    void shouldNotNeedArgs() {
        assertFalse(actionInstance.isNeedsArgs());
    }
    
    @Test
    void shouldNotNeedPermissions() {
        assertFalse(actionInstance.isRequiresPermissions());
    }
    
    @Test
    void shouldGetProximityAlerts() throws JSONException {
        //no LocationUpdates
        actionInstance.run();
        
        verify(locationConfigHelper).getProximityAlerts();
        verify(jsonProximityAlert).toJson(anyMap());
        verify(callbackContext).success((String) isNull());
        
        doReturn(requests).when(jsonProximityAlert).toJson(anyMap());
        
        actionInstance.run();
        
        verify(locationConfigHelper, times(2)).getProximityAlerts();
        verify(jsonProximityAlert, times(2)).toJson(anyMap());
        verify(callbackContext).success(requests);
    }
}


