package com.dff.cordova.plugin.location.dagger.modules;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Messenger;
import android.os.Process;

import com.dff.cordova.plugin.location.dagger.annotations.LocationRequestHandlerThread;
import com.dff.cordova.plugin.location.dagger.annotations.LocationRequestLooper;
import com.dff.cordova.plugin.location.dagger.annotations.LocationServiceHandlerThread;
import com.dff.cordova.plugin.location.dagger.annotations.LocationServiceLooper;
import com.dff.cordova.plugin.location.dagger.annotations.LocationServiceMessenger;
import com.dff.cordova.plugin.location.handlers.LocationServiceHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by anahas on 22.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 22.06.17
 */
@Module
public class PluginModule {

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
