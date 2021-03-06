package com.dff.cordova.plugin.location.plugin.listeners.callback;

import com.dff.cordova.plugin.location.core.events.LocationStatusChangedEvent;
import com.dff.cordova.plugin.location.core.json.events.JsonLocationStatusChangedEvent;
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
class OnLocationStatusChangedCallbackHandlerUnitTest {
    @Mock
    private Log log;
    
    @Mock
    private EventBus eventBus;
    
    @Mock
    private CallbackHandlerHelper callbackHandlerHelper;
    
    @Mock
    private JsonLocationStatusChangedEvent jsonLocationStatusChangedEvent;
    
    @Mock
    private LocationStatusChangedEvent locationStatusChangedEvent;
    
    @Mock
    private JSONObject mEventToJson;
    
    @Mock
    private CallbackContext callbackContext;
    
    @InjectMocks
    private OnLocationStatusChangedCallbackHandler onLocationStatusChangedCallbackHandler;
    
    @Test
    void shouldRegisterEventBus() {
        onLocationStatusChangedCallbackHandler.onInitialize();

        verify(eventBus)
            .register(onLocationStatusChangedCallbackHandler);
    }
    
    @Test
    void whenOnDestroyUnregisterEventBus() {
        verify(eventBus, never())
            .unregister(onLocationStatusChangedCallbackHandler);
        onLocationStatusChangedCallbackHandler.onDestroy();
        
        verify(eventBus)
            .unregister(onLocationStatusChangedCallbackHandler);
    }
    
    @Test
    void shouldSendPluginResultOnEvent() throws JSONException {
        onLocationStatusChangedCallbackHandler.setCallback(callbackContext);
        
        doReturn(mEventToJson).when(jsonLocationStatusChangedEvent).toJson(locationStatusChangedEvent);
        onLocationStatusChangedCallbackHandler.onLocationStatusChangedEvent(locationStatusChangedEvent);
        
        verify(callbackHandlerHelper)
            .sendPluginResult(callbackContext, mEventToJson);
    }
}

