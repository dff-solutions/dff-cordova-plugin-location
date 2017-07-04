package com.dff.cordova.plugin.location;

import android.app.Application;

import com.dff.cordova.plugin.location.dagger.components.DaggerPluginComponent;
import com.dff.cordova.plugin.location.dagger.modules.TestAppModule;
import com.dff.cordova.plugin.location.dagger.modules.TestCordovaModule;
import com.dff.cordova.plugin.location.dagger.modules.TestPluginModule;

import org.apache.cordova.CordovaInterface;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Created by anahas on 03.07.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 03.07.17
 */

public class LocationPluginBaseTest {

    @Mock
    Application application;

    @Mock
    CordovaInterface cordova;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        System.out.println("setup config test");
//        Dagger
//            .builder()
//            .appModule(new TestAppModule(application))
//            .cordovaModule(new TestCordovaModule(cordova))
//            .pluginModule(new TestPluginModule())
//            .build()
//            .inject(this);
    }

    @Test
    public void test() {
        System.out.println("setup config test");
    }
}
