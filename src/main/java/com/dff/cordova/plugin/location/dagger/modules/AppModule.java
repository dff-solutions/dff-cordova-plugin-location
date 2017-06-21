package com.dff.cordova.plugin.location.dagger.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Messenger;
import android.os.Process;
import android.preference.PreferenceManager;

import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.dagger.annotations.DefaultUncaughException;
import com.dff.cordova.plugin.location.dagger.annotations.LocationRequestHandlerThread;
import com.dff.cordova.plugin.location.dagger.annotations.LocationRequestLooper;
import com.dff.cordova.plugin.location.dagger.annotations.LocationServiceHandlerThread;
import com.dff.cordova.plugin.location.dagger.annotations.LocationServiceLooper;
import com.dff.cordova.plugin.location.dagger.annotations.LocationServiceMessenger;
import com.dff.cordova.plugin.location.dagger.annotations.Private;
import com.dff.cordova.plugin.location.dagger.annotations.Shared;
import com.dff.cordova.plugin.location.handlers.LocationServiceHandler;
import com.dff.cordova.plugin.location.resources.LocationResources;

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

// TODO: 21.06.2017 split app module to plugin and app module

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
        return mApp.getSharedPreferences(LocationResources.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    Handler provideHandler() {
        return new Handler();
    }

    @Provides
    @Singleton
    @LocationServiceHandlerThread
    HandlerThread provideLocationServiceHandlerThread() {
        return new HandlerThread("@LocationServiceHandler", Process.THREAD_PRIORITY_BACKGROUND);
    }

    @Provides
    @LocationServiceLooper
    Looper provideLooper(Handler handler) {
        return handler.getLooper();
    }

    @Provides
    @LocationServiceMessenger
    Messenger provideLocationServiceMessenger(LocationServiceHandler locationServiceHandler) {
        return new Messenger(locationServiceHandler);
    }

    @Provides
    @Singleton
    @LocationRequestHandlerThread
    HandlerThread provideLocationRequestHandlerThread() {
        HandlerThread handlerThread = new HandlerThread("@LocationRequestHandlerThread",
            Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        return handlerThread;
    }

    @Provides
    @LocationRequestLooper
    Looper provideLocationRequestLooper(@LocationRequestHandlerThread HandlerThread handlerThread) {
        return handlerThread.getLooper();
    }
}
