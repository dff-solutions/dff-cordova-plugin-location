package com.dff.cordova.plugin.location.plugin.action;

import android.location.LocationManager;
import android.location.LocationProvider;

import com.dff.cordova.plugin.location.core.json.classes.JsonLocationProvider;
import com.dff.cordova.plugin.shared.classes.json.JsonThrowable;
import com.dff.cordova.plugin.shared.helpers.JsonFactory;
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

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GetAllProvidersUnitTest {
    
    private ArrayList<String> providers;
    
    @Mock
    private Log log;
    
    @Mock
    private CallbackContext callbackContext;
    
    @Mock
    private JSONObject provider2Json;
    
    @Mock
    private JsonLocationProvider jsonLocationProvider;
    
    @Mock
    private LocationManager locationManager;
    
    @Mock
    private LocationProvider locationProvider;
    
    @Mock
    private JsonThrowable jsonThrowable;
    
    @Mock
    private JsonFactory jsonFactory;
    
    @Mock
    private JSONArray allProviders2json;
    
    @InjectMocks
    private GetAllProviders actionInstance;
    
    private Class<? extends LocationAction> actionClass = GetAllProviders.class;
    
    @BeforeEach
    void setup() {
        actionInstance.setLog(log);
        actionInstance.setAction(actionInstance.getActionName());
        actionInstance.setCallbackContext(callbackContext);
        actionInstance.setJsonThrowable(jsonThrowable);
        
        providers = new ArrayList<String>();
        providers.add("murx");
        
    }
    
    @Test
    void actionName_shouldBeLowerClassName() {
        char[] c = actionClass.getSimpleName().toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        String classNameLower = new String(c);
        
        assertEquals(classNameLower, GetAllProviders.ACTION);
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
    void shouldGetAllProvider() throws JSONException {
        actionInstance.run();
        verify(callbackContext).success((String) isNull());
        
        doReturn(allProviders2json).when(jsonFactory).getJSONArray();
        doReturn(providers).when(locationManager).getAllProviders();
        doReturn(locationProvider).when(locationManager).getProvider(anyString());
        doReturn(provider2Json).when(jsonLocationProvider).toJson(locationProvider);
        
        actionInstance.run();
        
        verify(callbackContext).success(same(allProviders2json));
    }
}
