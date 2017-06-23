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
import com.dff.cordova.plugin.location.resources.Res;

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

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApp;
    }

    @Provides
    Application provideApplication() {
        return mApp;
    }

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @Singleton
    @DefaultUncaughException
    Thread.UncaughtExceptionHandler provideDefaultThreadUncaughtExceptionHandler() {
        return Thread.getDefaultUncaughtExceptionHandler();
    }

    // Dagger will only look for methods annotated with @Provides
    // Application reference must come from AppModule.class
    @Provides
    @Singleton
    @Shared
    SharedPreferences providesSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mApp);
    }

    @Provides
    @Singleton
    @Private
    SharedPreferences provideprivateSharedPreferences() {
        return mApp.getSharedPreferences(Res.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    Handler provideHandler() {
        return new Handler();
    }
}
