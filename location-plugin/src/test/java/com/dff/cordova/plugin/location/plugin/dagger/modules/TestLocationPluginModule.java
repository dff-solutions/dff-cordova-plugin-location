package com.dff.cordova.plugin.location.plugin.dagger.modules;

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
import com.dff.cordova.plugin.location.plugin.dagger.annotations.LocationPluginScope;
import com.dff.cordova.plugin.shared.classes.json.JsonThrowable;
import com.dff.cordova.plugin.shared.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.shared.dagger.annotations.PreferencesName;
import com.dff.cordova.plugin.shared.helpers.AndroidSdkHelper;
import com.dff.cordova.plugin.shared.helpers.CallbackHandlerHelper;
import com.dff.cordova.plugin.shared.helpers.ConfigTemplateHelper;
import com.dff.cordova.plugin.shared.helpers.FileFactory;
import com.dff.cordova.plugin.shared.helpers.JsonFactory;
import com.dff.cordova.plugin.shared.helpers.PermissionHelper;
import com.dff.cordova.plugin.shared.helpers.SharedPreferencesHelper;
import com.dff.cordova.plugin.shared.helpers.StringTemplateHelper;
import com.dff.cordova.plugin.shared.log.Log;

import org.greenrobot.eventbus.EventBus;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmConfiguration;

@Module
public class TestLocationPluginModule {
    public Log log;
    public JsonThrowable jsonThrowable;
    public GeoLocationRealmHelper geoLocationRealmHelper;
    public PermissionHelper permissionHelper;
    public LocationRealmHelper locationRealmHelper;
    public LocationRealmConfigBuilder locationRealmConfigBuilder;
    public FileFactory fileFactory;
    public LocationConfigHelper locationConfigHelper;
    public LocationModule locationModule;
    public LocationMigration locationMigration;
    public ConfigTemplateHelper configTemplateHelper;
    public JsonLocationConfig jsonLocationConfig;
    public JsonFactory jsonFactory;
    public JsonLocationUpdateRequest jsonLocationUpdateRequest;
    public JsonProximityAlert jsonProximityAlert;
    public JsonLocationDbConfig jsonLocationDbConfig;
    public JsonCriteria jsonCriteria;
    public LocationUpdateRequestFactory locationUpdateRequestFactory;
    public StringTemplateHelper stringTemplateHelper;
    public LocationConfigUpdateManager locationConfigUpdateManager;
    public LocationUpdateManager locationUpdateManager;
    public LocationIntentFactory locationIntentFactory;
    public LocationForegroundManager locationForegroundManager;
    public JsonLocationProvider jsonLocationProvider;
    public LocationDiagnosticsHelper locationDiagnosticsHelper;
    public JsonLocationDiagnostics jsonLocationDiagnostics;
    public JsonLocation jsonLocation;
    public JsonGeoLocation jsonGeoLocation;
    public ProximityAlertEventRealmHelper proximityAlertEventRealmHelper;
    public CallbackHandlerHelper callbackHandlerHelper;
    public AndroidSdkHelper androidSdkHelper;
    public EventBus eventBus;
    public JsonProximityAlertEvent jsonProximityAlertEvent;
    
    @Provides
    Log provideLog() {
        return log;
    }
    
    @Provides
    JsonThrowable provideJsonThrowable() {
        return jsonThrowable;
    }
    
    @Provides
    RealmConfiguration.Builder provideRealmConfigurationBuild() {
        return new RealmConfiguration.Builder();
    }
    
    @Provides
    @LocationPluginScope
    SharedPreferencesHelper provideSharedPreferencesHelper(
        @ApplicationContext Context context,
        @PreferencesName String preferencesName
    ) {
        return new SharedPreferencesHelper(context, preferencesName);
    }
    
    @Provides
    LocationManager provideLocationManager(@ApplicationContext Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }
    
    @Provides
    GeoLocationRealmHelper provideGeoLocationRealmHelper() {
        return geoLocationRealmHelper;
    }
    
    @Provides
    PermissionHelper providePermissionHelper() {
        return permissionHelper;
    }
    
    @Provides
    LocationRealmHelper provideLocationRealmHelper() {
        return locationRealmHelper;
    }
    
    @Provides
    LocationRealmConfigBuilder provideLocationRealmConfigBuilder() {
        return locationRealmConfigBuilder;
    }
    
    @Provides
    FileFactory provideFileFactory() {
        return fileFactory;
    }
    
    @Provides
    LocationConfigHelper provideLocationConfigHelper() {
        return locationConfigHelper;
    }
    
    @Provides
    LocationModule provideLocationModule() {
        return locationModule;
    }
    
    @Provides
    LocationMigration provideLocationMigration() {
        return locationMigration;
    }
    
    @Provides
    ConfigTemplateHelper provideConfigTemplateHelper() {
        return configTemplateHelper;
    }
    
    @Provides
    JsonLocationConfig provideJsonLocationConfig() {
        return jsonLocationConfig;
    }
    
    @Provides
    JsonFactory provideJsonFactory() {
        return jsonFactory;
    }
    
    @Provides
    JsonLocationUpdateRequest provideJsonLocationUpdateRequest() {
        return jsonLocationUpdateRequest;
    }
    
    @Provides
    JsonProximityAlert provideJsonProximityAlert() {
        return jsonProximityAlert;
    }
    
    @Provides
    JsonLocationDbConfig provideJsonLocationDbConfig() {
        return jsonLocationDbConfig;
    }
    
    @Provides
    JsonCriteria provideJsonCriteria() {
        return jsonCriteria;
    }
    
    @Provides
    LocationUpdateRequestFactory provideLocationUpdateRequestFactory() {
        return locationUpdateRequestFactory;
    }
    
    @Provides
    StringTemplateHelper provideStringTemplateHelper() {
        return stringTemplateHelper;
    }
    
    @Provides
    LocationConfigUpdateManager provideLocationConfigUpdateManager() {
        return locationConfigUpdateManager;
    }
    
    @Provides
    LocationUpdateManager provideLocationUpdateManager() {
        return locationUpdateManager;
    }
    
    @Provides
    LocationIntentFactory provideLocationIntentFactory() {
        return locationIntentFactory;
    }
    
    @Provides
    LocationForegroundManager provideLocationForegroundManager() {
        return locationForegroundManager;
    }
    
    @Provides
    JsonLocationProvider provideJsonLocationProvider() {
        return jsonLocationProvider;
    }
    
    @Provides
    LocationDiagnosticsHelper provideLocationDiagnosticsHelper() {
        return locationDiagnosticsHelper;
    }
    
    @Provides
    JsonLocationDiagnostics provideJsonLocationDiagnostics() {
        return jsonLocationDiagnostics;
    }
    
    @Provides
    JsonLocation provideJsonLocation() {
        return jsonLocation;
    }
    
    @Provides
    JsonGeoLocation provideJsonGeoLocation() {
        return jsonGeoLocation;
    }
    
    @Provides
    ProximityAlertEventRealmHelper provideProximityAlertEventRealmHelper(){
        return proximityAlertEventRealmHelper;
    }
    
    @Provides
    CallbackHandlerHelper provideCallbackHandlerHelper(){
       return callbackHandlerHelper;
    }

    @Provides
    AndroidSdkHelper provideAndroidSdkHelper() {
        return androidSdkHelper;
    }
    
    @Provides
    EventBus provideEventBus() {
        return eventBus;
    }
    
    @Provides
    JsonProximityAlertEvent provideJsonProximityAlertEvent() {
        return jsonProximityAlertEvent;
    }
}
