package com.dff.cordova.plugin.location;

import com.dff.cordova.plugin.location.resources.Res;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by anahas on 06.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 06.06.17
 */

public class LocationPluginUniTest {

    LocationPlugin mLocationPlugin;

    //	Tells Mockito to mock the objects
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

    @BeforeClass
    public static void setupOnce() {
        System.out.println("starting with the test");
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(cordova.getThreadPool()).thenReturn(executorService);
        mLocationPlugin = new LocationPlugin();
        mLocationPlugin.setCordovaInterface(cordova);
    }

    @Test
    public void testAllJSActionOfExecution() throws JSONException {
        List<String> jsActionsList
            = new ArrayList<>(Arrays.asList(Res.ALL_JS_ACTIONS));

        boolean foundAndExecuted;

        for (String jsAction : jsActionsList) {
            foundAndExecuted = mLocationPlugin.execute(jsAction, args, callbackContext);
            assertTrue("Action "
                    + jsAction
                    + " has been found and executed ? --> ",
                foundAndExecuted);
        }
    }

    @Test
    public void testAnUnknownAction() throws JSONException {
        Random random = new Random();
        assertFalse("Unkown action should return false",
            mLocationPlugin.execute("UNKNOWN" + random.nextGaussian(), args, callbackContext));
    }
}
