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

import static com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest.JSON_ARG_CRITERIA;
import static com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest.JSON_ARG_ID;
import static com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest.JSON_ARG_MIN_DISTANCE;
import static com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest.JSON_ARG_MIN_TIME;
import static com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest.JSON_ARG_PROVIDER;
import static com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest.JSON_ARG_SINGLE;
import static junit.framework.TestCase.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestLocationUpdateUnitTest {
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
    private LocationUpdateRequest request;
    
    @Mock
    private JsonLocationUpdateRequest jsonLocationUpdateRequest;
    
    @Mock
    private LocationConfigUpdateManager locationConfigUpdateManager;
    
    @Mock
    private PermissionHelper permissionHelper;
    
    @InjectMocks
    private RequestLocationUpdate actionInstance;
    
    private Class<? extends LocationAction> actionClass = RequestLocationUpdate.class;
    private final String requestId = "id";
    
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
        
        assertEquals(classNameLower, RequestLocationUpdate.ACTION);
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
    void shouldRequestLocationUpdate() throws JSONException {
        doReturn(true).when(permissionHelper).hasAllPermissions(any());
        // no args array
        assertNull(actionInstance.getArgs());
        when(jsonThrowable.toJson(any(Throwable.class))).thenReturn(jsonError);
        actionInstance.run();
        verify(callbackContext).error(jsonError);
        
        doReturn(jsonargs).when(args).optJSONObject(0);
        doReturn(true).when(jsonargs).has(JSON_ARG_ID);
        doReturn(true).when(jsonargs).has(JSON_ARG_MIN_TIME);
        doReturn(true).when(jsonargs).has(JSON_ARG_MIN_DISTANCE);
        doReturn(true).when(jsonargs).has(JSON_ARG_SINGLE);
        
        doReturn(request).when(jsonLocationUpdateRequest).fromJson(jsonargs);
        doReturn(requestId).when(request).getId();
        
        actionInstance.setArgs(args);
        actionInstance.run();
        
        verify(jsonLocationUpdateRequest).fromJson(jsonargs);
        verify(locationConfigUpdateManager).addLocationUpdateRequest(request);
        verify(callbackContext).success(requestId);
    }
    
    @Test
    void shouldThrowException() throws JSONException {
        doReturn(true).when(permissionHelper).hasAllPermissions(any());
        doReturn(jsonargs).when(args).optJSONObject(0);
        doReturn(true).when(jsonargs).has(JSON_ARG_ID);
        doReturn(true).when(jsonargs).has(JSON_ARG_MIN_TIME);
        doReturn(true).when(jsonargs).has(JSON_ARG_MIN_DISTANCE);
        doReturn(true).when(jsonargs).has(JSON_ARG_SINGLE);
        
        doReturn(true).when(jsonargs).isNull(JSON_ARG_CRITERIA);
        doReturn(true).when(jsonargs).isNull(JSON_ARG_PROVIDER);
        
        doReturn(jsonError).when(jsonThrowable).toJson((Throwable) any());
        
        actionInstance.setArgs(args);
        actionInstance.run();
        
        verify(callbackContext).error(jsonError);
    }
}

