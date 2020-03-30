package com.dff.cordova.plugin.location.plugin.action;

import com.dff.cordova.plugin.location.core.config.LocationConfigUpdateManager;
import com.dff.cordova.plugin.shared.classes.json.JsonThrowable;
import com.dff.cordova.plugin.shared.log.Log;

import org.apache.cordova.CallbackContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ResumeLocationUpdatesUnitTest {
    @Mock
    private Log log;
    
    @Mock
    private CallbackContext callbackContext;
    
    @Mock
    private JsonThrowable jsonThrowable;
    
    @Mock
    private LocationConfigUpdateManager locationConfigUpdateManager;
    
    @InjectMocks
    private Resume actionInstance;
    
    private Class<? extends LocationAction> actionClass = Resume.class;
    
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
        
        assertEquals(classNameLower, Resume.ACTION);
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
    void shouldResumeLocationUpdate() {
        actionInstance.run();
        verify(locationConfigUpdateManager).resume();
        verify(callbackContext).success();
    }
}
