package com.dff.cordova.plugin.location.plugin.listeners.callback;

import com.dff.cordova.plugin.location.core.events.LocationStatusChangedEvent;
import com.dff.cordova.plugin.location.core.json.events.JsonLocationStatusChangedEvent;
import com.dff.cordova.plugin.location.plugin.dagger.annotations.LocationPluginScope;
import com.dff.cordova.plugin.shared.helpers.CallbackHandlerHelper;
import com.dff.cordova.plugin.shared.log.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import javax.inject.Inject;

/**
 * Callback handler for location status changed events.
 */
@LocationPluginScope
public class OnLocationStatusChangedCallbackHandler extends CallbackHandler {
    private static final String TAG = "OnLocationStatusChangedCBH";
    
    private EventBus eventBus;
    private JsonLocationStatusChangedEvent jsonLocationStatusChangedEvent;
    private CallbackHandlerHelper callbackHandlerHelper;
    
    @Inject
    public OnLocationStatusChangedCallbackHandler(
        Log log,
        EventBus eventBus,
        JsonLocationStatusChangedEvent jsonLocationStatusChangedEvent,
        CallbackHandlerHelper callbackHandlerHelper
    ) {
        super(log);
        this.eventBus = eventBus;
        this.jsonLocationStatusChangedEvent = jsonLocationStatusChangedEvent;
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
     * Sends a location status changed event to the cordova plugin.
     *
     * @param event location status changed event
     */
    @Subscribe
    public void onLocationStatusChangedEvent(LocationStatusChangedEvent event) {
        try {
            callbackHandlerHelper
                .sendPluginResult(callback, jsonLocationStatusChangedEvent.toJson(event));
        } catch (JSONException e) {
            log.e(TAG, e.getMessage(), e);
            callbackHandlerHelper.sendPluginResult(callback, e);
        }
    }
}
