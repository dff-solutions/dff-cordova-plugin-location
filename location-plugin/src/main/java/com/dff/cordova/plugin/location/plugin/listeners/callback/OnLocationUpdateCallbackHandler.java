package com.dff.cordova.plugin.location.plugin.listeners.callback;

import com.dff.cordova.plugin.location.core.events.LocationUpdateEvent;
import com.dff.cordova.plugin.location.core.json.events.JsonLocationUpdateEvent;
import com.dff.cordova.plugin.location.plugin.dagger.annotations.LocationPluginScope;
import com.dff.cordova.plugin.shared.helpers.CallbackHandlerHelper;
import com.dff.cordova.plugin.shared.log.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import javax.inject.Inject;

/**
 * Callback handler for location update events.
 */
@LocationPluginScope
public class OnLocationUpdateCallbackHandler extends CallbackHandler {
    private static final String TAG = "OnShuttleSendSuccessCBH";
    
    private EventBus eventBus;
    private JsonLocationUpdateEvent jsonLocationUpdateEvent;
    private CallbackHandlerHelper callbackHandlerHelper;
    
    @Inject
    public OnLocationUpdateCallbackHandler(
        Log log,
        EventBus eventBus,
        JsonLocationUpdateEvent jsonLocationUpdateEvent,
        CallbackHandlerHelper callbackHandlerHelper
    ) {
        super(log);
        this.eventBus = eventBus;
        this.jsonLocationUpdateEvent = jsonLocationUpdateEvent;
        this.callbackHandlerHelper = callbackHandlerHelper;
    }

    /**
     * Initialize callback handler.
     */
    public void onInitialize() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }
    
    /**
     * Sends a location update event to the cordova plugin.
     *
     * @param event location update event
     */
    @Subscribe
    public void onLocationUpdateEvent(LocationUpdateEvent event) {
        try {
            callbackHandlerHelper
                .sendPluginResult(callback, jsonLocationUpdateEvent.toJson(event));
        } catch (JSONException e) {
            log.e(TAG, e.getMessage(), e);
            callbackHandlerHelper.sendPluginResult(callback, e);
        }
    }
    
}
