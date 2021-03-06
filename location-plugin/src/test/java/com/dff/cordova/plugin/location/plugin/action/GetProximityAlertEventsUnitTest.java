package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.helpers.realm.ProximityAlertEventRealmHelper;
import com.dff.cordova.plugin.location.core.json.realm.models.JsonProximityAlertEvent;
import com.dff.cordova.plugin.location.core.realm.models.ProximityAlertEvent;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GetProximityAlertEventsUnitTest {
    @Mock
    private Log log;
    
    @Mock
    private CallbackContext callbackContext;
    
    @Mock
    private ProximityAlertEventRealmHelper proximityAlertEventRealmHelper;
    
    @Mock
    private JsonProximityAlertEvent jsonProximityAlertEvent;
    
    @Mock
    private JSONArray jsonArray;
    
    @Mock
    private JSONArray args;
    
    @Mock
    private List<ProximityAlertEvent> proximityAlertEventList;
    
    @Mock
    private JsonThrowable jsonThrowable;
    
    @Mock
    private PermissionHelper permissionHelper;
    
    @Mock
    private JSONObject jsonargs;
    
    @InjectMocks
    private GetProximityAlertEvents actionInstance;
    
    private Class<? extends LocationAction> actionClass = GetProximityAlertEvents.class;
    
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
        
        assertEquals(classNameLower, GetProximityAlertEvents.ACTION);
        assertEquals(classNameLower, actionInstance.getActionName());
    }
    
    @Test
    void shouldNotNeedArgs() {
        assertFalse(actionInstance.isNeedsArgs());
    }
    
    @Test
    void shouldNeedPermissions() {
        assertTrue(actionInstance.isRequiresPermissions());
    }
    
    @Test
    void shouldGetProximityAlertEvents() throws JSONException {
        doReturn(true).when(permissionHelper).hasAllPermissions(any());
        
        doReturn(jsonargs).when(args).optJSONObject(0);
        doReturn(proximityAlertEventList).when(proximityAlertEventRealmHelper).findAll(jsonargs);
        doReturn(jsonArray).when(jsonProximityAlertEvent).toJson(proximityAlertEventList);
        
        actionInstance.setArgs(args);
        actionInstance.run();
        
        verify(callbackContext).success(jsonArray);
    }
}
