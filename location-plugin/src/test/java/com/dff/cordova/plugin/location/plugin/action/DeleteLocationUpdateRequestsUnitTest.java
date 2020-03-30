package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.config.LocationConfigUpdateManager;
import com.dff.cordova.plugin.location.core.config.LocationUpdateRequest;
import com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest;
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
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteLocationUpdateRequestsUnitTest {
    @Mock
    private Log log;
    
    @Mock
    private CallbackContext callbackContext;
    
    @Mock
    private JSONArray args;
    
    @Mock
    private JSONObject jsonargs;

    @Mock
    private LocationConfigUpdateManager locationConfigUpdateManager;
    
    @Mock
    private JsonLocationUpdateRequest jsonLocationUpdateRequest;
    
    @Mock
    private JSONObject jsonObject;
    
    @Mock
    private JsonThrowable jsonThrowable;
    
    @Mock
    private LocationUpdateRequest locationUpdateRequest;
    
    @Mock
    private PermissionHelper permissionHelper;
    
    @Mock
    private JSONObject jsonError;
    
    @InjectMocks
    private DeleteLocationUpdateRequest actionInstance;
    
    private Class<? extends LocationAction> actionClass = DeleteLocationUpdateRequest.class;
    private String requestId = "requestId";
    
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
        
        assertEquals(classNameLower, DeleteLocationUpdateRequest.ACTION);
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
    void shouldDeleteLocationUpdateRequests() throws JSONException {
        doReturn(true).when(permissionHelper).hasAllPermissions(any());
        
        // no args array
        assertNull(actionInstance.getArgs());
        when(jsonThrowable.toJson(any(Throwable.class))).thenReturn(jsonError);
        actionInstance.run();
        verify(callbackContext).error(jsonError);
        
        //unable to remove
        doReturn(jsonargs).when(args).optJSONObject(0);
        doReturn(true).when(jsonargs).has(JSON_ARG_ID);
        actionInstance.setArgs(args);
        actionInstance.run();
        verify(callbackContext).success((String) null);
        
        //delete request
        doReturn(requestId).when(jsonargs).getString(JSON_ARG_ID);
        doReturn(locationUpdateRequest).when(locationConfigUpdateManager).removeLocationUpdateRequest(requestId);
        doReturn(jsonObject).when(jsonLocationUpdateRequest).toJson(locationUpdateRequest);
        
        actionInstance.run();
        verify(callbackContext).success(jsonObject);
    }
}
