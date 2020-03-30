package com.dff.cordova.plugin.location.plugin.action;

import android.location.LocationManager;

import com.dff.cordova.plugin.shared.classes.json.JsonThrowable;
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

import static com.dff.cordova.plugin.location.plugin.action.GetProvider.JSON_ARG_PROVIDER;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IsProviderEnabledUnitTest {
    @Mock
    private Log log;
    
    @Mock
    private CallbackContext callbackContext;
    
    @Mock
    private JSONArray args;
    
    @Mock
    private JSONObject jsonargs;
    
    @Mock
    private LocationManager locationManager;
    
    @Mock
    private JsonThrowable jsonThrowable;
    
    @Mock
    private JSONObject jsonError;
    
    @InjectMocks
    private IsProviderEnabled actionInstance;
    
    private Class<? extends LocationAction> actionClass = IsProviderEnabled.class;
    private final String providerName = "provider";
    
    @BeforeEach
    void setup() {
        actionInstance.setLog(log);
        actionInstance.setAction(actionInstance.getActionName());
        actionInstance.setCallbackContext(callbackContext);
        actionInstance.setJsonThrowable(jsonThrowable);
    }
    
    @Test
    void actionName_shouldBeLowerClassName() {
        char[] c = actionClass.getSimpleName().toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        String classNameLower = new String(c);
        
        assertEquals(classNameLower, IsProviderEnabled.ACTION);
        assertEquals(classNameLower, actionInstance.getActionName());
    }
    
    @Test
    void shouldNeedArgs() {
        assertTrue(actionInstance.isNeedsArgs());
    }
    
    @Test
    void shouldNotNeedPermissions() {
        assertFalse(actionInstance.isRequiresPermissions());
    }
    
    @Test
    void shouldIsProviderEnabled() throws JSONException {
        // no args array
        assertNull(actionInstance.getArgs());
        when(jsonThrowable.toJson(any(Throwable.class))).thenReturn(jsonError);
        actionInstance.run();
        verify(callbackContext).error(jsonError);
        
        doReturn(jsonargs).when(args).optJSONObject(0);
        doReturn(true).when(jsonargs).has(JSON_ARG_PROVIDER);
        actionInstance.setArgs(args);
        actionInstance.run();
        verify(callbackContext).success(0);
        
        doReturn(providerName).when(jsonargs).getString(JSON_ARG_PROVIDER);
        doReturn(true).when(locationManager).isProviderEnabled(anyString());
        
        actionInstance.run();
        
        verify(callbackContext).success(1);
    }
}

