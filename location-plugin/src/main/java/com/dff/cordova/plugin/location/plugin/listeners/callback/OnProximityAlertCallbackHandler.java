package com.dff.cordova.plugin.location.plugin.listeners.callback;

import com.dff.cordova.plugin.location.core.json.realm.models.JsonProximityAlertEvent;
import com.dff.cordova.plugin.location.core.realm.models.ProximityAlertEvent;
import com.dff.cordova.plugin.location.plugin.dagger.annotations.LocationPluginScope;
import com.dff.cordova.plugin.shared.helpers.CallbackHandlerHelper;
import com.dff.cordova.plugin.shared.log.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import javax.inject.Inject;

/**
 * Callback handler for proximity alert events.
 */
@LocationPluginScope
public class OnProximityAlertCallbackHandler extends CallbackHandler {
    private static final String TAG = "onProximityAlertCBH";
    
    private EventBus eventBus;
    private JsonProximityAlertEvent jsonProximityAlertEvent;
    private CallbackHandlerHelper callbackHandlerHelper;
    
    @Inject
    public OnProximityAlertCallbackHandler(
        Log log,
        EventBus eventBus,
        JsonProximityAlertEvent jsonProximityAlertEvent,
        CallbackHandlerHelper callbackHandlerHelper
    ) {
        super(log);
        this.eventBus = eventBus;
        this.jsonProximityAlertEvent = jsonProximityAlertEvent;
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
     * Sends a proximity alert event to the cordova plugin.
     *
     * @param event proximity alert event
     */
    @Subscribe
    public void onProximityAlertEvent(ProximityAlertEvent event) {
        try {
            callbackHandlerHelper
                .sendPluginResult(callback, jsonProximityAlertEvent.toJson(event));
        } catch (JSONException e) {
            log.e(TAG, e.getMessage(), e);
            callbackHandlerHelper.sendPluginResult(callback, e);
        }
    }
}
