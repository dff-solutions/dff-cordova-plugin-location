package com.dff.cordova.plugin.location.dagger.modules;

import android.os.HandlerThread;
import android.os.Looper;

import com.dff.cordova.plugin.location.dagger.annotations.LocationRequestHandlerThread;
import com.dff.cordova.plugin.location.dagger.annotations.LocationServiceLooper;

import org.mockito.Mockito;

import dagger.Module;

import static org.mockito.Mockito.when;


/**
 * Created by anahas on 29.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 29.06.17
 */
public class TestPluginModule extends PluginModule {

    @Override
    HandlerThread provideLocationRequestHandlerThread() {

        return Mockito.mock(HandlerThread.class);
    }

    @Override
    Looper provideLocationRequestLooper(@LocationRequestHandlerThread HandlerThread handlerThread) {
        when(handlerThread.getLooper()).thenReturn(Mockito.mock(Looper.class));
        return handlerThread.getLooper();
    }

    @Override
    Looper provideLocationRequestLooper2(@LocationServiceLooper HandlerThread handlerThread) {
        when(handlerThread.getLooper()).thenReturn(Mockito.mock(Looper.class));
        return handlerThread.getLooper();
    }
}
