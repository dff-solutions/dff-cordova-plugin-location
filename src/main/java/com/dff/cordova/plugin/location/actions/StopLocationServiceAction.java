package com.dff.cordova.plugin.location.actions;

import com.dff.cordova.plugin.common.action.Action;
import com.dff.cordova.plugin.location.events.OnStopLocationService;
import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Stop location service
 *
 * @author Anthony Nahas
 * @version 2.0
 * @since 19.06.17
 */
@Singleton
public class StopLocationServiceAction extends Action {

    private EventBus mEventBus;

    @Inject
    public StopLocationServiceAction(
        EventBus mEventBus) {
        this.mEventBus = mEventBus;
    }

    @Override
    public void execute() {
        mEventBus.post(new OnStopLocationService(callbackContext));
    }
}
