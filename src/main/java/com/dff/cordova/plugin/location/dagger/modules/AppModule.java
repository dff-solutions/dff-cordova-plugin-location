package com.dff.cordova.plugin.location.dagger.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.dagger.annotations.DefaultUncaughException;
import com.dff.cordova.plugin.location.dagger.annotations.Private;
import com.dff.cordova.plugin.location.dagger.annotations.Shared;
import com.dff.cordova.plugin.location.resources.Resources;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * The @Module annotation tells Dagger that the AppModule class will provide dependencies for a part
 * of the mApp. It is normal to have multiple Dagger modules in a project, and it is typical
 * for one of them to provide app-wide dependencies.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 12.06.17
 */
@Module
public class AppModule {

    private Application mApp;

    public AppModule(Application app) {
        this.mApp = app;
    }

    public Application getApp() {
        return mApp;
    }

    @Provides
    @ApplicationContext
    public Context provideContext() {
        return mApp;
    }

    @Provides
    public Application provideApplication() {
        return mApp;
    }

    @Provides
    @Singleton
    public EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @Singleton
    @DefaultUncaughException
    public Thread.UncaughtExceptionHandler provideDefaultThreadUncaughtExceptionHandler() {
        return Thread.getDefaultUncaughtExceptionHandler();
    }

    // Dagger will only look for methods annotated with @Provides
    // Application reference must come from AppModule.class
    @Provides
    @Singleton
    @Shared
    public SharedPreferences providesSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mApp);
    }

    @Provides
    @Singleton
    @Private
    public SharedPreferences providePrivateSharedPreferences() {
        return mApp.getSharedPreferences(Resources.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    public Handler provideHandler() {
        return new Handler();
    }
}
