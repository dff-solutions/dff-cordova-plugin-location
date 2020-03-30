package com.dff.cordova.plugin.location.plugin.action.realm;

import com.dff.cordova.plugin.location.core.helpers.realm.LocationRealmHelper;
import com.dff.cordova.plugin.shared.classes.json.JsonThrowable;
import com.dff.cordova.plugin.shared.helpers.PermissionHelper;
import com.dff.cordova.plugin.shared.log.Log;

import org.apache.cordova.CallbackContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeleteAllUnitTest {
    @Mock
    Log log;
    
    @Mock
    CallbackContext callbackContext;
    
    @Mock
    JsonThrowable jsonThrowable;
    
    @Mock
    LocationRealmHelper locationRealmHelper;
    
    @Mock
    PermissionHelper permissionHelper;
    
    @InjectMocks
    private DeleteAll actionInstance;
    
    private Class<? extends RealmAction> actionClass = DeleteAll.class;
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
                     DeleteAll.ACTION);
        Assertions.assertEquals(RealmAction.ACTION_PREFIX + classNameLower,
                                actionInstance.getActionName());
    }
    
    @Test
    void shouldNotNeedArgs() {
        Assertions.assertFalse(actionInstance.isNeedsArgs());
    }
    
    @Test
    void shouldNotNeedPermissions() {
        Assertions.assertTrue(actionInstance.isRequiresPermissions());
    }
    
    @Test
    void shouldDeleteAll() {
        doReturn(true).when(permissionHelper).hasAllPermissions(any());
        
        actionInstance.run();
        
        verify(locationRealmHelper).deleteAll();
        verify(callbackContext).success();
    }
}
