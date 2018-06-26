package com.dff.cordova.plugin.location.dagger.components;

import com.dff.cordova.plugin.location.LocationPlugin;
import com.dff.cordova.plugin.location.dagger.modules.AppModule;
import com.dff.cordova.plugin.location.dagger.modules.CordovaModule;
import com.dff.cordova.plugin.location.dagger.modules.RealmModule;

import dagger.Component;

import javax.inject.Singleton;

/**
 * Plugin component for DI
 *
 * @author Anthony Nahas
 * @version 1.1
 * @since 13.06.17
 */

@Singleton
@Component(modules =
        {
                AppModule.class,
                CordovaModule.class,
                RealmModule.class
        })
public interface LocationPluginComponent {

    void inject(LocationPlugin locationPlugin);
}