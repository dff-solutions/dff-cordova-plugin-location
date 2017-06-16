package com.dff.cordova.plugin.location.dagger.components;

import android.app.Service;

import com.dff.cordova.plugin.location.LocationPlugin;
import com.dff.cordova.plugin.location.dagger.modules.ActivityModule;
import com.dff.cordova.plugin.location.dagger.modules.AppModule;
import com.dff.cordova.plugin.location.services.LocationService;
import com.dff.cordova.plugin.location.services.PendingLocationsIntentService;


import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by anahas on 13.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 13.06.17
 */

@Singleton
@Component(modules =
    {
        AppModule.class,
        ActivityModule.class
    })
public interface PluginComponent {

    void inject(LocationPlugin locationPlugin);

    void inject(LocationService locationService);

    void inject(PendingLocationsIntentService pendingLocationsIntentService);
}