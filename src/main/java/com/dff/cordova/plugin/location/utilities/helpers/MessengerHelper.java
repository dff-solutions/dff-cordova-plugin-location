package com.dff.cordova.plugin.location.utilities.helpers;

import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.common.service.ServiceHandler;
import com.dff.cordova.plugin.location.events.OnLocationServiceBindEvent;

import org.apache.cordova.CallbackContext;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by anahas on 19.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 19.06.17
 */
@Singleton
public class MessengerHelper {

    private static final String TAG = MessengerHelper.class.getSimpleName();

    private EventBus mEventBus;
    private ServiceHandler mServiceHandler;
    private Messenger mMessenger;

    @Inject
    public MessengerHelper(EventBus mEventBus, ServiceHandler mServiceHandler) {
        this.mEventBus = mEventBus;
        this.mServiceHandler = mServiceHandler;

        mEventBus.register(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onLocationServiceBind(OnLocationServiceBindEvent event) {
        this.mMessenger = mServiceHandler.getService();
    }

    public void send(Message msg, CallbackContext callbackContext) {
        try {
            if (mMessenger != null) {
                mMessenger.send(msg);
            }
        } catch (RemoteException | NullPointerException e) {
            CordovaPluginLog.e(TAG, "Error: ", e);
            callbackContext.error("Error while sending a message within the location service: " + e);
        }
    }
}
