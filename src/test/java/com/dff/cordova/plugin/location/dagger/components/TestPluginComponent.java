package com.dff.cordova.plugin.location.dagger.components;


import com.dff.cordova.plugin.location.LocationPluginTest;
import com.dff.cordova.plugin.location.dagger.modules.AppModule;
import com.dff.cordova.plugin.location.dagger.modules.CordovaModule;
import com.dff.cordova.plugin.location.dagger.modules.PluginModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by anahas on 28.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 28.06.17
 */
@Singleton
@Component(modules =
    {
        AppModule.class,
        CordovaModule.class,
        PluginModule.class
    })
public interface TestPluginComponent {

    void inject(LocationPluginTest test);
}
