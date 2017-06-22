package com.dff.cordova.plugin.location.dagger.components;


import com.dff.cordova.plugin.location.dagger.modules.AppModule;
import com.dff.cordova.plugin.location.dagger.modules.ServiceModule;
import com.dff.cordova.plugin.location.services.LocationService;
import com.dff.cordova.plugin.location.services.PendingLocationsIntentService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by anahas on 22.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 22.06.17
 */
@Singleton
@Component(modules = {
    AppModule.class,
    ServiceModule.class
})

public interface ServiceComponent {

    void inject(LocationService locationService);

    void inject(PendingLocationsIntentService pendingLocationsIntentService);
}