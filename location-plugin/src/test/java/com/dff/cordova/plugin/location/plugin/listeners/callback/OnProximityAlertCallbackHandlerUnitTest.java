package com.dff.cordova.plugin.location.plugin.listeners.callback;

import com.dff.cordova.plugin.location.core.json.realm.models.JsonProximityAlertEvent;
import com.dff.cordova.plugin.location.core.realm.models.ProximityAlertEvent;
import com.dff.cordova.plugin.shared.helpers.CallbackHandlerHelper;
import com.dff.cordova.plugin.shared.log.Log;

import org.apache.cordova.CallbackContext;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OnProximityAlertCallbackHandlerUnitTest {
    @Mock
    private Log log;
    
    @Mock
    private EventBus eventBus;
    
    @Mock
    private CallbackHandlerHelper callbackHandlerHelper;
    
    @Mock
    private JsonProximityAlertEvent jsonProximityAlertEvent;
    
    @Mock
    private ProximityAlertEvent proximityAlertEvent;
    
    @Mock
    private JSONObject mEventToJson;
    
    @Mock
    private CallbackContext callbackContext;
    
    @InjectMocks
    private OnProximityAlertCallbackHandler onProximityAlertCallbackHandler;
    
    @Test
    void shouldRegisterEventBus() {
        onProximityAlertCallbackHandler.onInitialize();

        verify(eventBus)
            .register(onProximityAlertCallbackHandler);
    }
    
    @Test
    void whenOnDestroyUnregisterEventBus() {
        verify(eventBus, never())
            .unregister(onProximityAlertCallbackHandler);
        onProximityAlertCallbackHandler.onDestroy();
        
        verify(eventBus)
            .unregister(onProximityAlertCallbackHandler);
    }
    
    @Test
    void shouldSendPluginResultOnEvent() throws JSONException {
        onProximityAlertCallbackHandler.setCallback(callbackContext);
        
        doReturn(mEventToJson).when(jsonProximityAlertEvent).toJson(proximityAlertEvent);
        onProximityAlertCallbackHandler.onProximityAlertEvent(proximityAlertEvent);
        
        verify(callbackHandlerHelper)
            .sendPluginResult(callbackContext, mEventToJson);
    }
}
