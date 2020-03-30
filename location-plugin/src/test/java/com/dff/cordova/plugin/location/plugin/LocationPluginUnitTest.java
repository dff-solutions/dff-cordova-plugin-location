package com.dff.cordova.plugin.location.plugin;

import android.app.Activity;
import android.content.Context;

import com.dff.cordova.plugin.location.plugin.dagger.LocationPluginDaggerManager;
import com.dff.cordova.plugin.location.plugin.listeners.callback.OnLocationStatusChangedCallbackHandler;
import com.dff.cordova.plugin.location.plugin.listeners.callback.OnLocationUpdateCallbackHandler;
import com.dff.cordova.plugin.location.plugin.listeners.callback.OnProviderEnabledCallbackHandler;
import com.dff.cordova.plugin.location.plugin.listeners.callback.OnProximityAlertCallbackHandler;
import com.dff.cordova.plugin.shared.configurations.ActionsManager;
import com.dff.cordova.plugin.shared.helpers.PermissionHelper;
import com.dff.cordova.plugin.shared.log.Log;

import org.apache.cordova.CordovaInterface;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import static com.dff.cordova.plugin.location.plugin.LocationPlugin.PERMISSION_REQUEST_CODE;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@PrepareForTest(LocationPluginDaggerManager.class)
public class LocationPluginUnitTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Mock
    private Log log;

    @Mock
    private OnLocationStatusChangedCallbackHandler onLocationStatusChangedCallbackHandler;

    @Mock
    private OnLocationUpdateCallbackHandler onLocationUpdateCallbackHandler;

    @Mock
    private OnProviderEnabledCallbackHandler onProviderEnabledCallbackHandler;

    @Mock
    private OnProximityAlertCallbackHandler onProximityAlertCallbackHandler;

    @Mock
    private ActionsManager actionsManager;

    @Mock
    private PermissionHelper permissionHelper;

    @Mock
    private LocationPluginDaggerManager daggerManager;

    @Mock
    private CordovaInterface cordovaInterface;

    @Mock
    private Activity activity;

    @Mock
    private Context context;

    private final String[] permissions = new String[] {};

    private LocationPlugin locationPlugin;

    @Before
    public void init() {
        PowerMockito.mockStatic(LocationPluginDaggerManager.class);

        locationPlugin = new LocationPlugin();
        locationPlugin.log = log;
        locationPlugin.onLocationUpdateCallbackHandler = onLocationUpdateCallbackHandler;
        locationPlugin.onProviderEnabledCallbackHandler = onProviderEnabledCallbackHandler;
        locationPlugin.onLocationStatusChangedCallbackHandler = onLocationStatusChangedCallbackHandler;
        locationPlugin.onProximityAlertCallbackHandler = onProximityAlertCallbackHandler;
        locationPlugin.actionsManager = actionsManager;
        locationPlugin.permissionHelper = permissionHelper;
        locationPlugin.pluginPermissions = permissions;
        locationPlugin.cordova = cordovaInterface;
    }

    @Test
    public void pluginInitialize_shouldInjectAndInitializeCBH() {
        PowerMockito.when(LocationPluginDaggerManager.getInstance()).thenReturn(daggerManager);

        doReturn(activity).when(cordovaInterface).getActivity();
        doReturn(context).when(activity).getApplicationContext();
        doReturn(daggerManager).when(daggerManager).context(context);

        locationPlugin.pluginInitialize();

        verify(daggerManager).inject(locationPlugin);
        verify(daggerManager).register(locationPlugin);
        verify(onLocationStatusChangedCallbackHandler).onInitialize();
        verify(onLocationUpdateCallbackHandler).onInitialize();
        verify(onProviderEnabledCallbackHandler).onInitialize();
        verify(onProximityAlertCallbackHandler).onInitialize();
    }

    @Test
    public void onDestroy_shouldCallOnDestroyOnDependencies() {
        PowerMockito.when(LocationPluginDaggerManager.getInstance()).thenReturn(daggerManager);

        locationPlugin.onDestroy();
        verify(actionsManager).onDestroy();
        verify(onLocationStatusChangedCallbackHandler).onDestroy();
        verify(onLocationUpdateCallbackHandler).onDestroy();
        verify(onProviderEnabledCallbackHandler).onDestroy();
        verify(onProximityAlertCallbackHandler).onDestroy();

        verify(daggerManager).unregister(locationPlugin);
    }

    @Test
    public void onResume_shouldRequestPermissions() {
        doReturn(activity).when(cordovaInterface).getActivity();
        doReturn(false).when(permissionHelper).hasAllPermissions(permissions);
        doReturn(false).when(permissionHelper).shouldShowRequestPermissionRationale(activity,
            permissions);

        locationPlugin.onResume(true);

        verify(cordovaInterface).requestPermissions(same(locationPlugin),
            eq(PERMISSION_REQUEST_CODE), eq(permissions));

        locationPlugin.onResume(true);

        verify(cordovaInterface).requestPermissions(same(locationPlugin),
            eq(PERMISSION_REQUEST_CODE), eq(permissions));

        doReturn(true).when(permissionHelper).shouldShowRequestPermissionRationale(activity,
            permissions);

        locationPlugin.onResume(true);

        verify(cordovaInterface, times(2)).requestPermissions(same(locationPlugin),
            eq(PERMISSION_REQUEST_CODE), eq(permissions));
    }
}
