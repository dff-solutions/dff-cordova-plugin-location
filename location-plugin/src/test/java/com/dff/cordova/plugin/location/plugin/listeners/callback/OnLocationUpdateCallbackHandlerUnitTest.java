package com.dff.cordova.plugin.location.plugin.listeners.callback;

import com.dff.cordova.plugin.location.core.events.LocationUpdateEvent;
import com.dff.cordova.plugin.location.core.json.events.JsonLocationUpdateEvent;
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
class OnLocationUpdateCallbackHandlerUnitTest {
    @Mock
    private Log log;
    
    @Mock
    private EventBus eventBus;

    @Mock
    private CallbackHandlerHelper callbackHandlerHelper;
    
    @Mock
    private JsonLocationUpdateEvent jsonLocationUpdateEvent;
    
    @Mock
    private LocationUpdateEvent locationUpdateEvent;
    
    @Mock
    private JSONObject mEventToJson;
    
    @Mock
    private CallbackContext callbackContext;
    
    @InjectMocks
    private OnLocationUpdateCallbackHandler onLocationUpdateCallbackHandler;
    
    @Test
    void shouldRegisterEventBus() {
        onLocationUpdateCallbackHandler.onInitialize();

        verify(eventBus)
            .register(onLocationUpdateCallbackHandler);
    }
    
    @Test
    void whenOnDestroyUnregisterEventBus() {
        verify(eventBus, never())
            .unregister(onLocationUpdateCallbackHandler);
        onLocationUpdateCallbackHandler.onDestroy();
        
        verify(eventBus)
            .unregister(onLocationUpdateCallbackHandler);
    }
    
    @Test
    void shouldSendPluginResultOnEvent() throws JSONException {
        onLocationUpdateCallbackHandler.setCallback(callbackContext);
        
        doReturn(mEventToJson).when(jsonLocationUpdateEvent).toJson(locationUpdateEvent);
        onLocationUpdateCallbackHandler.onLocationUpdateEvent(locationUpdateEvent);
        
        verify(callbackHandlerHelper)
            .sendPluginResult(callbackContext, mEventToJson);
    }
}
