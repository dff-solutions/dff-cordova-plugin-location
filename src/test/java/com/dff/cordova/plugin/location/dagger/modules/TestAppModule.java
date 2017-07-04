package com.dff.cordova.plugin.location.dagger.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import org.greenrobot.eventbus.EventBus;
import org.mockito.Mockito;

import dagger.Module;

/**
 * Created by anahas on 28.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 28.06.17
 */
public class TestAppModule extends AppModule {

    //app will be mocked in test scenario
    public TestAppModule(Application app) {
        super(app);
    }

    @Override
    public Context provideContext() {
        return super.getApp();
    }

    @Override
    public Application provideApplication() {
        return super.getApp();
    }

    @Override
    public EventBus provideEventBus() {
        return Mockito.mock(EventBus.class);
    }

    @Override
    public Thread.UncaughtExceptionHandler provideDefaultThreadUncaughtExceptionHandler() {
        return Mockito.mock(Thread.UncaughtExceptionHandler.class);
    }

    @Override
    public SharedPreferences providesSharedPreferences() {
        return Mockito.mock(SharedPreferences.class);
    }

    @Override
    public SharedPreferences providePrivateSharedPreferences() {
        return Mockito.mock(SharedPreferences.class);
    }

    @Override
    public Handler provideHandler() {
        return Mockito.mock(Handler.class);
    }
}
