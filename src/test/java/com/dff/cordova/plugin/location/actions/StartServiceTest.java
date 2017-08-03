package com.dff.cordova.plugin.location.actions;

import android.content.Context;
import android.content.Intent;

import com.dff.cordova.plugin.location.LocationPluginTest;
import com.dff.cordova.plugin.location.classes.Executor;
import com.dff.cordova.plugin.location.configurations.ActionsManager;
import com.dff.cordova.plugin.location.configurations.JSActions;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.handlers.LocationRequestHandler;
import com.dff.cordova.plugin.location.services.LocationService;
import com.dff.cordova.plugin.location.utilities.helpers.MessengerHelper;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    MessengerHelper mMessengerHelper;

    @Mock
    PreferencesHelper mPreferencesHelper;

    @Mock
    LocationRequestHandler mLocationRequestHandler;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    public StartServiceTest() {
        super();
        Dagger.inject(this);
    }

    @Test
    public void checkStartServiceActionValue() {
        assertEquals("start service action should be available in js actions class--> ",
            mJsActions.start_service,
            mStartServiceAction);
    }

    @Test
    public void checkStartServiceActionsMapping() {
        assertNotNull("start service action should be mapped in the action manager class",
            mActionsManager.hash(mStartServiceAction));
    }

    @Test
    public void checkStartServiceActionClass() {
        assertEquals("start service action class should be returned by the action manager after hashing",
            mActionsManager.hash(mStartServiceAction).getClass(), StartLocationServiceAction.class);
    }

    @Test
    public void checkStartServiceActionExecutionMock() {

        mExecutor.execute(mockedAction);

        verify(mockedAction, times(1)).execute();
    }

    @Test
    public void checkStartServiceActionExecution() {

        StartLocationServiceAction action = spy((StartLocationServiceAction)
            mActionsManager
                .hash(mStartServiceAction)
                .with(callbackContext)
                .andHasArguments(args));


        mExecutor.execute(action);
        verify(action, times(1)).execute();
    }

    @Test
    public void checkIntentFiredToStartService() {
        Context context = spy(mContext);
        StartLocationServiceAction action = new StartLocationServiceAction(
            context,
            mMessengerHelper,
            null,
            mLocationRequestHandler);

        mExecutor.execute(
            action
                .with(callbackContext)
                .andHasArguments(args));
        verify(context, times(1)).startService(ArgumentMatchers.<Intent>any());
    }
}
