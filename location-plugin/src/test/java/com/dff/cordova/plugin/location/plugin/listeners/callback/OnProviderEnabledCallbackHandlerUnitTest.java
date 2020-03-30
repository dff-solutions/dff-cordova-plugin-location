package com.dff.cordova.plugin.location.plugin.listeners.callback;

import com.dff.cordova.plugin.location.core.events.ProviderEnabledEvent;
import com.dff.cordova.plugin.location.core.json.events.JsonProviderEnabledEvent;
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
class OnProviderEnabledCallbackHandlerUnitTest {
    @Mock
    private Log log;
    
    @Mock
    private EventBus eventBus;
    
    @Mock
    private CallbackHandlerHelper callbackHandlerHelper;
    
    @Mock
    private JsonProviderEnabledEvent jsonProviderEnabledEvent;
    
    @Mock
    private ProviderEnabledEvent providerEnabledEvent;
    
    @Mock
    private JSONObject mEventToJson;
    
    @Mock
    private CallbackContext callbackContext;
    
    @InjectMocks
    private OnProviderEnabledCallbackHandler onProviderEnabledCallbackHandler;
    
    @Test
    void shouldRegisterEventBus() {
        onProviderEnabledCallbackHandler.onInitialize();

        verify(eventBus)
            .register(onProviderEnabledCallbackHandler);
    }
    
    @Test
    void whenOnDestroyUnregisterEventBus() {
        verify(eventBus, never())
            .unregister(onProviderEnabledCallbackHandler);
        onProviderEnabledCallbackHandler.onDestroy();
        
        verify(eventBus)
            .unregister(onProviderEnabledCallbackHandler);
    }
    
    @Test
    void shouldSendPluginResultOnEvent() throws JSONException {
        onProviderEnabledCallbackHandler.setCallback(callbackContext);
        
        doReturn(mEventToJson).when(jsonProviderEnabledEvent).toJson(providerEnabledEvent);
        onProviderEnabledCallbackHandler.onProviderEnabledEvent(providerEnabledEvent);
        
        verify(callbackHandlerHelper)
            .sendPluginResult(callbackContext, mEventToJson);
    }
}

