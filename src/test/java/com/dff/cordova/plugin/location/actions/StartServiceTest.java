package com.dff.cordova.plugin.location.actions;

import android.content.Context;
import android.content.Intent;
import com.dff.cordova.plugin.location.LocationPluginTest;
import com.dff.cordova.plugin.location.classes.Executor;
import com.dff.cordova.plugin.location.classes.GLocationManager;
import com.dff.cordova.plugin.location.configurations.ActionsManager;
import com.dff.cordova.plugin.location.configurations.JSActions;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.dagger.annotations.Shared;
import com.dff.cordova.plugin.location.resources.Resources;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;
import org.apache.cordova.CallbackContext;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.inject.Inject;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by anahas on 02.08.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 02.08.2017
 */

public class StartServiceTest extends LocationPluginTest {

    private final String mStartServiceAction = "location.action.START_SERVICE";

    @Inject
    @ApplicationContext
    Context mContext;

    @Inject
    EventBus mEventBus;

    @Inject
    @Shared
    GLocationManager mGLocationManager;

    @Inject
    JSActions mJsActions;

    @Inject
    ActionsManager mActionsManager;

    @Inject
    Executor mExecutor;

    @Mock
    StartLocationServiceAction mockedAction;

    @Mock
    JSONArray args;

    @Mock
    CallbackContext callbackContext;

    @Mock
    PreferencesHelper mPreferencesHelper;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    public StartServiceTest() {
        super();
        Dagger.inject(this);
    }

    @Test
    public void checkStartServiceActionValue() throws Exception {
        assertEquals("start service action should be available in js actions classes--> ",
            mJsActions.start_service,
            mStartServiceAction);
    }

    @Test
    public void checkStartServiceActionsMapping() throws Exception {
        assertNotNull("start service action should be mapped in the action manager classes",
            mActionsManager.hash(mStartServiceAction));
    }

    @Test
    public void checkStartServiceActionClass() throws Exception {
        assertEquals("start service action classes should be returned by the action manager after hashing",
            mActionsManager.hash(mStartServiceAction).getClass(), StartLocationServiceAction.class);
    }

    @Test
    public void checkStartServiceActionExecutionMock() throws Exception {

        mExecutor.execute(null,mockedAction);

        verify(mockedAction, times(1)).execute();
    }

    @Test
    public void checkStartServiceActionExecution() throws Exception {

        StartLocationServiceAction action = spy((StartLocationServiceAction)
            mActionsManager
                .hash(mStartServiceAction)
                .with(callbackContext)
                .andHasArguments(args));

        mExecutor.execute(action);
        verify(action, times(1)).execute();
    }

    @Test
    public void checkIntentFiredToStartService() throws Exception {
        Context context = spy(mContext);
        StartLocationServiceAction action = new StartLocationServiceAction(
            context,
            mGLocationManager,
            mEventBus,
            mPreferencesHelper);

        mExecutor.execute(
            action
                .with(callbackContext)
                .andHasArguments(args));
        verify(context, times(1)).startService(ArgumentMatchers.<Intent>any());
    }

    @Test
    public void checkArgumentsAndShouldStoreProperties() throws JSONException {

        JSONObject json = new JSONObject();
        json.put(Resources.MIN_TIME, 30);
        json.put(Resources.MIN_DISTANCE, 50);
        json.put(Resources.MIN_ACCURACY, 20);
        json.put(Resources.MAX_AGE, 30);
        json.put(Resources.DELAY, 50);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(json);

//        when(json.optLong(Resources.MIN_TIME)).thenReturn((long) 30);
//        when(json.optDouble(Resources.MIN_DISTANCE)).thenReturn((double) 50);
//        when(json.optInt(Resources.MIN_ACCURACY)).thenReturn(20);
//        when(json.optInt(Resources.MAX_AGE)).thenReturn(30);
//        when(json.optInt(Resources.DELAY)).thenReturn(50);

//        when(jsonArray.getJSONObject(0)).thenReturn(json);

        PreferencesHelper preferencesHelper = spy(mPreferencesHelper);

        StartLocationServiceAction action = new StartLocationServiceAction(
            mContext,
            mGLocationManager,
            mEventBus,
            preferencesHelper);

        mExecutor.execute(
            action
                .with(callbackContext)
                .andHasArguments(jsonArray));

        assertTrue("min time from json should be saved", Resources.LOCATION_MIN_TIME == 30);
        assertTrue("min distance from json should be saved", Resources.LOCATION_MIN_DISTANCE == 50);
        assertTrue("min accuracy from json should be saved", Resources.LOCATION_MIN_ACCURACY == 20);
        assertTrue("max age from json should be saved", Resources.LOCATION_MAX_AGE == 30);
        verify(preferencesHelper).storeProperties();
    }
}
