package com.dff.cordova.plugin.location;

import android.app.Application;

import com.dff.cordova.plugin.location.configurations.ActionsManager;
import com.dff.cordova.plugin.location.configurations.JSActions;


import com.dff.cordova.plugin.location.dagger.components.DaggerTestPluginComponent;
import com.dff.cordova.plugin.location.dagger.modules.TestAppModule;
import com.dff.cordova.plugin.location.dagger.modules.TestCordovaModule;
import com.dff.cordova.plugin.location.dagger.modules.TestPluginModule;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

/**
 * Created by anahas on 06.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 06.06.17
 */
@RunWith(RobolectricTestRunner.class)
public abstract class LocationPluginTest {

    private Application mApplication = RuntimeEnvironment.application;

    @Inject
    JSActions mJsActions;

    @Inject
    ActionsManager mActionsManager;

    @Mock
    CordovaInterface cordova;

    @Mock
    ExecutorService executorService;

    @Mock
    JSONArray args;

    @Mock
    CallbackContext callbackContext;

    // Tells Mockito to create the mocks based on the @Mock annotation
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Before
    public void setUp() {

        DaggerTestPluginComponent
            .builder()
            .appModule(new TestAppModule(mApplication))
            .cordovaModule(new TestCordovaModule(cordova))
            .pluginModule(new TestPluginModule())
            .build()
            .inject(this);
    }

    @BeforeClass
    public static void setupOnce() {
        System.out.println("starting with the test");
    }
}