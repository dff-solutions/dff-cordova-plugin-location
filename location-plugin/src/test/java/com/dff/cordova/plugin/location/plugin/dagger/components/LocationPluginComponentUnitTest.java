package com.dff.cordova.plugin.location.plugin.dagger.components;

import android.content.Context;
import android.location.LocationManager;

import com.dff.cordova.plugin.location.core.config.LocationConfigHelper;
import com.dff.cordova.plugin.location.core.config.LocationConfigUpdateManager;
import com.dff.cordova.plugin.location.core.config.LocationUpdateManager;
import com.dff.cordova.plugin.location.core.config.LocationUpdateRequestFactory;
import com.dff.cordova.plugin.location.core.helpers.LocationDiagnosticsHelper;
import com.dff.cordova.plugin.location.core.helpers.realm.GeoLocationRealmHelper;
import com.dff.cordova.plugin.location.core.helpers.realm.LocationRealmConfigBuilder;
import com.dff.cordova.plugin.location.core.helpers.realm.LocationRealmHelper;
import com.dff.cordova.plugin.location.core.helpers.realm.ProximityAlertEventRealmHelper;
import com.dff.cordova.plugin.location.core.intents.LocationIntentFactory;
import com.dff.cordova.plugin.location.core.json.classes.JsonCriteria;
import com.dff.cordova.plugin.location.core.json.classes.JsonLocation;
import com.dff.cordova.plugin.location.core.json.classes.JsonLocationConfig;
import com.dff.cordova.plugin.location.core.json.classes.JsonLocationDbConfig;
import com.dff.cordova.plugin.location.core.json.classes.JsonLocationDiagnostics;
import com.dff.cordova.plugin.location.core.json.classes.JsonLocationProvider;
import com.dff.cordova.plugin.location.core.json.classes.JsonLocationUpdateRequest;
import com.dff.cordova.plugin.location.core.json.classes.JsonProximityAlert;
import com.dff.cordova.plugin.location.core.json.realm.models.JsonGeoLocation;
import com.dff.cordova.plugin.location.core.json.realm.models.JsonProximityAlertEvent;
import com.dff.cordova.plugin.location.core.realm.migrations.LocationMigration;
import com.dff.cordova.plugin.location.core.realm.schemes.LocationModule;
import com.dff.cordova.plugin.location.core.services.LocationForegroundManager;
import com.dff.cordova.plugin.location.plugin.action.DeleteLocationUpdateRequest;
import com.dff.cordova.plugin.location.plugin.action.DeleteLocations;
import com.dff.cordova.plugin.location.plugin.action.DeleteProximityAlert;
import com.dff.cordova.plugin.location.plugin.action.DeleteProximityAlertEvents;
import com.dff.cordova.plugin.location.plugin.action.GetAllProviders;
import com.dff.cordova.plugin.location.plugin.action.GetConfig;
import com.dff.cordova.plugin.location.plugin.action.GetLastKnownLocation;
import com.dff.cordova.plugin.location.plugin.action.GetLocationUpdateRequests;
import com.dff.cordova.plugin.location.plugin.action.GetLocations;
import com.dff.cordova.plugin.location.plugin.action.GetProvider;
import com.dff.cordova.plugin.location.plugin.action.GetProximityAlertEvents;
import com.dff.cordova.plugin.location.plugin.action.GetProximityAlerts;
import com.dff.cordova.plugin.location.plugin.action.IsLocationEnabled;
import com.dff.cordova.plugin.location.plugin.action.IsProviderEnabled;
import com.dff.cordova.plugin.location.plugin.action.LocationAction;
import com.dff.cordova.plugin.location.plugin.action.OnLocationStatusChanged;
import com.dff.cordova.plugin.location.plugin.action.OnLocationUpdate;
import com.dff.cordova.plugin.location.plugin.action.OnProviderEnabled;
import com.dff.cordova.plugin.location.plugin.action.OnProximityAlert;
import com.dff.cordova.plugin.location.plugin.action.Pause;
import com.dff.cordova.plugin.location.plugin.action.ReadDiagnostics;
import com.dff.cordova.plugin.location.plugin.action.RequestLocationUpdate;
import com.dff.cordova.plugin.location.plugin.action.RequestProximityAlert;
import com.dff.cordova.plugin.location.plugin.action.Resume;
import com.dff.cordova.plugin.location.plugin.action.SetConfig;
import com.dff.cordova.plugin.location.plugin.action.StartForeground;
import com.dff.cordova.plugin.location.plugin.action.StopForeground;
import com.dff.cordova.plugin.location.plugin.action.realm.Compact;
import com.dff.cordova.plugin.location.plugin.action.realm.Delete;
import com.dff.cordova.plugin.location.plugin.action.realm.DeleteAll;
import com.dff.cordova.plugin.location.plugin.action.realm.GetDatabaseInformation;
import com.dff.cordova.plugin.location.plugin.dagger.modules.TestLocationPluginModule;
import com.dff.cordova.plugin.shared.classes.json.JsonThrowable;
import com.dff.cordova.plugin.shared.helpers.AndroidSdkHelper;
import com.dff.cordova.plugin.shared.helpers.CallbackHandlerHelper;
import com.dff.cordova.plugin.shared.helpers.ConfigTemplateHelper;
import com.dff.cordova.plugin.shared.helpers.FileFactory;
import com.dff.cordova.plugin.shared.helpers.JsonFactory;
import com.dff.cordova.plugin.shared.helpers.PermissionHelper;
import com.dff.cordova.plugin.shared.helpers.StringTemplateHelper;
import com.dff.cordova.plugin.shared.log.Log;

import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Provider;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class LocationPluginComponentUnitTest {
    @Mock
    private LocationManager locationManager;
    
    @Mock
    private Context context;
    
    @Mock
    private Log log;
    
    @Mock
    private JsonThrowable jsonThrowable;
    
    @Mock
    private GeoLocationRealmHelper geoLocationRealmHelper;
    
    @Mock
    private PermissionHelper permissionHelper;
    
    @Mock
    private LocationRealmHelper locationRealmHelper;
    
    @Mock
    private LocationRealmConfigBuilder locationRealmConfigBuilder;
    
    @Mock
    private FileFactory fileFactory;
    
    @Mock
    private LocationConfigHelper locationConfigHelper;
    
    @Mock
    private LocationModule locationModule;
    
    @Mock
    private LocationMigration locationMigration;
    
    @Mock
    private ConfigTemplateHelper configTemplateHelper;
    
    @Mock
    private JsonLocationConfig jsonLocationConfig;
    
    @Mock
    private JsonFactory jsonFactory;
    
    @Mock
    private JsonLocationUpdateRequest jsonLocationUpdateRequest;
    
    @Mock
    private JsonProximityAlert jsonProximityAlert;
    
    @Mock
    private JsonLocationDbConfig jsonLocationDbConfig;
    
    @Mock
    private JsonCriteria jsonCriteria;
    
    @Mock
    private LocationUpdateRequestFactory locationUpdateRequestFactory;
    
    @Mock
    private StringTemplateHelper stringTemplateHelper;
    
    @Mock
    private LocationConfigUpdateManager locationConfigUpdateManager;
    
    @Mock
    private LocationUpdateManager locationUpdateManager;
    
    @Mock
    private LocationIntentFactory locationIntentFactory;
    
    @Mock
    private LocationForegroundManager locationForegroundManager;
    
    @Mock
    private JsonLocationProvider jsonLocationProvider;
    
    @Mock
    private LocationDiagnosticsHelper locationDiagnosticsHelper;
    
    @Mock
    private JsonLocationDiagnostics jsonLocationDiagnostics;
    
    @Mock
    private JsonLocation jsonLocation;
    
    @Mock
    private JsonGeoLocation jsonGeoLocation;
    
    @Mock
    private ProximityAlertEventRealmHelper proximityAlertEventRealmHelper;
    
    @Mock
    private CallbackHandlerHelper callbackHandlerHelper;

    @Mock
    private AndroidSdkHelper androidSdkHelper;
    
    @Mock
    private EventBus eventBus;
    
    @Mock
    private JsonProximityAlertEvent jsonProximityAlertEvent;
    
    private final String[] permissions = new String[0];
    private final String preferenceName = "preferenceName";
    
    private TestLocationPluginComponent testLocationPluginComponent;
    
    @BeforeEach
    void setup() {
        TestLocationPluginModule testLocationPluginModule = new TestLocationPluginModule();
        
        testLocationPluginModule.log = log;
        testLocationPluginModule.jsonThrowable = jsonThrowable;
        testLocationPluginModule.geoLocationRealmHelper = geoLocationRealmHelper;
        testLocationPluginModule.permissionHelper = permissionHelper;
        testLocationPluginModule.locationRealmHelper = locationRealmHelper;
        testLocationPluginModule.locationRealmConfigBuilder = locationRealmConfigBuilder;
        testLocationPluginModule.fileFactory = fileFactory;
        testLocationPluginModule.locationConfigHelper = locationConfigHelper;
        testLocationPluginModule.locationModule = locationModule;
        testLocationPluginModule.locationMigration = locationMigration;
        testLocationPluginModule.configTemplateHelper = configTemplateHelper;
        testLocationPluginModule.jsonLocationConfig = jsonLocationConfig;
        testLocationPluginModule.jsonFactory = jsonFactory;
        testLocationPluginModule.jsonLocationUpdateRequest = jsonLocationUpdateRequest;
        testLocationPluginModule.jsonProximityAlert = jsonProximityAlert;
        testLocationPluginModule.jsonLocationDbConfig = jsonLocationDbConfig;
        testLocationPluginModule.jsonCriteria = jsonCriteria;
        testLocationPluginModule.locationUpdateRequestFactory = locationUpdateRequestFactory;
        testLocationPluginModule.stringTemplateHelper = stringTemplateHelper;
        testLocationPluginModule.locationConfigUpdateManager = locationConfigUpdateManager;
        testLocationPluginModule.locationUpdateManager = locationUpdateManager;
        testLocationPluginModule.locationIntentFactory = locationIntentFactory;
        testLocationPluginModule.locationForegroundManager = locationForegroundManager;
        testLocationPluginModule.jsonLocationProvider = jsonLocationProvider;
        testLocationPluginModule.locationDiagnosticsHelper = locationDiagnosticsHelper;
        testLocationPluginModule.jsonLocationDiagnostics = jsonLocationDiagnostics;
        testLocationPluginModule.jsonLocation = jsonLocation;
        testLocationPluginModule.jsonGeoLocation = jsonGeoLocation;
        testLocationPluginModule.proximityAlertEventRealmHelper = proximityAlertEventRealmHelper;
        testLocationPluginModule.callbackHandlerHelper = callbackHandlerHelper;
        testLocationPluginModule.androidSdkHelper = androidSdkHelper;
        testLocationPluginModule.eventBus = eventBus;
        testLocationPluginModule.jsonProximityAlertEvent = jsonProximityAlertEvent;
        
        testLocationPluginComponent = DaggerTestLocationPluginComponent
            .builder()
            .testLocationPluginModule(testLocationPluginModule)
            .applicationContext(context)
            .pluginPermissions(permissions)
            .preferencesName(preferenceName)
            .build();
    }
    
    @Test
    void shouldBeDefined() {
        assertNotNull(testLocationPluginComponent);
    }
    
    @Test
    void shouldProvideActionProviders() {
        assertNotNull(testLocationPluginComponent.provideActionProviders());
    }
    
    @Test
    void actionProviders_shouldProvideActions() {
        doReturn(locationManager).when(context).getSystemService(Context.LOCATION_SERVICE);
        
        Map<String, Provider<? extends LocationAction>> actionProviders =
            testLocationPluginComponent.provideActionProviders();
    
        Map<String, Class<? extends LocationAction>> actions = new HashMap<>();
        actions.put(Compact.ACTION, Compact.class);
        actions.put(Delete.ACTION, Delete.class);
        actions.put(DeleteAll.ACTION, DeleteAll.class);
        actions.put(DeleteLocations.ACTION, DeleteLocations.class);
        actions.put(DeleteLocationUpdateRequest.ACTION, DeleteLocationUpdateRequest.class);
        actions.put(DeleteProximityAlert.ACTION, DeleteProximityAlert.class);
        actions.put(DeleteProximityAlertEvents.ACTION, DeleteProximityAlertEvents.class);
        actions.put(GetAllProviders.ACTION, GetAllProviders.class);
        actions.put(GetConfig.ACTION, GetConfig.class);
        actions.put(GetDatabaseInformation.ACTION, GetDatabaseInformation.class);
        actions.put(GetLastKnownLocation.ACTION, GetLastKnownLocation.class);
        actions.put(GetLocations.ACTION, GetLocations.class);
        actions.put(GetLocationUpdateRequests.ACTION, GetLocationUpdateRequests.class);
        actions.put(GetProvider.ACTION, GetProvider.class);
        actions.put(GetProximityAlertEvents.ACTION, GetProximityAlertEvents.class);
        actions.put(GetProximityAlerts.ACTION, GetProximityAlerts.class);
        actions.put(IsLocationEnabled.ACTION, IsLocationEnabled.class);
        actions.put(IsProviderEnabled.ACTION, IsProviderEnabled.class);
        actions.put(OnLocationStatusChanged.ACTION, OnLocationStatusChanged.class);
        actions.put(OnLocationUpdate.ACTION, OnLocationUpdate.class);
        actions.put(OnProviderEnabled.ACTION, OnProviderEnabled.class);
        actions.put(OnProximityAlert.ACTION, OnProximityAlert.class);
        actions.put(Pause.ACTION, Pause.class);
        actions.put(ReadDiagnostics.ACTION, ReadDiagnostics.class);
        actions.put(RequestLocationUpdate.ACTION, RequestLocationUpdate.class);
        actions.put(RequestProximityAlert.ACTION, RequestProximityAlert.class);
        actions.put(Resume.ACTION, Resume.class);
        actions.put(SetConfig.ACTION, SetConfig.class);
        actions.put(StartForeground.ACTION, StartForeground.class);
        actions.put(StopForeground.ACTION, StopForeground.class);
        
        for (Map.Entry<String, Class<? extends LocationAction>> e : actions.entrySet()) {
            Provider<? extends LocationAction> provider =
                actionProviders.get(e.getKey());
    
            LocationAction action1 = provider.get();
            LocationAction action2 = provider.get();
    
            assertNotNull(action1);
            assertNotNull(action2);
            assertThat(action1, instanceOf(e.getValue()));
            assertThat(action2, instanceOf(e.getValue()));
            assertNotSame(action1, action2);
        }
    }
    
}
