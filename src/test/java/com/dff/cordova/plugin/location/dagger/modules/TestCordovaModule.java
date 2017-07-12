package com.dff.cordova.plugin.location.dagger.modules;

import android.os.Messenger;

import com.dff.cordova.plugin.common.service.ServiceHandler;

import org.apache.cordova.CordovaInterface;
import org.mockito.Mockito;


import static org.mockito.Mockito.when;

/**
 * Created by anahas on 28.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 28.06.17
 */
public class TestCordovaModule extends CordovaModule {

    public TestCordovaModule(CordovaInterface mCordovaInterface) {
        super(mCordovaInterface);
    }

    @Override
    ServiceHandler provideServiceHandler() {
        return Mockito.mock(ServiceHandler.class);
    }

    @Override
    Messenger provideMessengerForServiceHandler(ServiceHandler serviceHandler) {
        when(serviceHandler.bindService()).thenReturn(true);
        when(serviceHandler.getService()).thenReturn(Mockito.mock(Messenger.class));
        return serviceHandler.getService();
    }
}
