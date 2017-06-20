package com.dff.cordova.plugin.location.dagger.modules;

import android.os.Messenger;

import com.dff.cordova.plugin.common.service.ServiceHandler;
import com.dff.cordova.plugin.location.dagger.annotations.ServiceHandlerMessenger;
import com.dff.cordova.plugin.location.services.LocationService;

import org.apache.cordova.CordovaInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by anahas on 16.06.2017
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 16.06.17
 */
@Module
public class CordovaModule {

    private CordovaInterface mCordovaInterface;

    public CordovaModule(CordovaInterface mCordovaInterface) {
        this.mCordovaInterface = mCordovaInterface;
    }

    @Provides
    @Singleton
    ServiceHandler provideServiceHandler() {
        ServiceHandler serviceHandler = new ServiceHandler(mCordovaInterface, LocationService.class);
        serviceHandler.bindService();
        return serviceHandler;
    }

    @Provides
    @ServiceHandlerMessenger
    Messenger provideMessengerForServiceHandler(ServiceHandler serviceHandler) {
        return serviceHandler.getService();
    }
}
