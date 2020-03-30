package com.dff.cordova.plugin.location.plugin.listeners.callback;

import com.dff.cordova.plugin.location.core.events.ProviderEnabledEvent;
import com.dff.cordova.plugin.location.core.json.events.JsonProviderEnabledEvent;
import com.dff.cordova.plugin.location.plugin.dagger.annotations.LocationPluginScope;
import com.dff.cordova.plugin.shared.helpers.CallbackHandlerHelper;
import com.dff.cordova.plugin.shared.log.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import javax.inject.Inject;

/**
 * Callback handler for provider enabled events.
 */
@LocationPluginScope
public class OnProviderEnabledCallbackHandler extends CallbackHandler {
    private static final String TAG = "OnProviderEnabledCBH";
    
    private EventBus eventBus;
    private JsonProviderEnabledEvent jsonProviderEnabledEvent;
    private CallbackHandlerHelper callbackHandlerHelper;
    
    @Inject
    public OnProviderEnabledCallbackHandler(
        Log log,
        EventBus eventBus,
        JsonProviderEnabledEvent jsonProviderEnabledEvent,
        CallbackHandlerHelper callbackHandlerHelper
    ) {
        super(log);
        this.eventBus = eventBus;
        this.jsonProviderEnabledEvent = jsonProviderEnabledEvent;
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
     * Sends a provider enabled event to the cordova plugin.
     *
     * @param event provider enabled event
     */
    @Subscribe
    public void onProviderEnabledEvent(ProviderEnabledEvent event) {
        try {
            callbackHandlerHelper
                .sendPluginResult(callback, jsonProviderEnabledEvent.toJson(event));
        } catch (JSONException e) {
            log.e(TAG, e.getMessage(), e);
            callbackHandlerHelper.sendPluginResult(callback, e);
        }
    }
}
