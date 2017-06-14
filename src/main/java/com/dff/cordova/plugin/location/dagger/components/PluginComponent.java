package com.dff.cordova.plugin.location.dagger.components;

import android.app.Application;
import android.content.Context;

import com.dff.cordova.plugin.location.LocationPlugin;
import com.dff.cordova.plugin.location.classes.Executor;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.dagger.modules.ActivityModule;
import com.dff.cordova.plugin.location.dagger.modules.AppModule;

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

    @ApplicationContext
    Context getContext();

    Executor getExecutor();

    Application getApplication();


}
