package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.config.LocationConfigHelper;
import com.dff.cordova.plugin.shared.classes.json.JsonThrowable;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GetConfigUnitTest {
    @Mock
    private Log log;
    
    @Mock
    private CallbackContext callbackContext;
    
    @Mock
    private JsonThrowable jsonThrowable;
    
    @Mock
    private LocationConfigHelper locationConfigHelper;
    
    @Mock
    private JSONObject config2Json;
    
    @InjectMocks
    private GetConfig actionInstance;
    
    private Class<? extends LocationAction> actionClass = GetConfig.class;
    
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
        
        assertEquals(classNameLower, GetConfig.ACTION);
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
    void shouldGetConfig() throws JSONException {
        doReturn(null).when(locationConfigHelper).getLocationConfigJson();
        
        actionInstance.run();
        
        verify(locationConfigHelper).getLocationConfigJson();
        verify(callbackContext).success((String) null);
        
        doReturn(config2Json).when(locationConfigHelper).getLocationConfigJson();
        
        actionInstance.run();
        
        verify(locationConfigHelper, times(2)).getLocationConfigJson();
        verify(callbackContext).success((String) null);
    }
}
