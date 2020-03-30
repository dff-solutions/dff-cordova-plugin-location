package com.dff.cordova.plugin.location.plugin.dagger.module;

import android.content.Context;
import android.content.Intent;

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
import com.dff.cordova.plugin.location.plugin.dagger.annotations.LocationPluginScope;
import com.dff.cordova.plugin.shared.configurations.ActionsManager;
import com.dff.cordova.plugin.shared.dagger.annotations.ActionHandlerServiceIntent;
import com.dff.cordova.plugin.shared.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.shared.log.Log;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Provider;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module for the location plugin. Provides a map that contains all location actions.
 */
@Module
public class LocationPluginModule {
    @Provides
    @LocationPluginScope
    ActionsManager provideActionsManager(
        @ApplicationContext Context context,
        @ActionHandlerServiceIntent Intent intent,
        Log log,
        Map<String, Provider<? extends LocationAction>> actionsMap
    ) {
        return new ActionsManager<>(context, intent, log, actionsMap);
    }
    
    @Provides
    @LocationPluginScope
    Map<String, Provider<? extends LocationAction>> provideActionProviders(
        Provider<Compact> compactProvider,
        Provider<Delete> deleteProvider,
        Provider<DeleteAll> deleteAllProvider,
        Provider<DeleteLocations> deleteLocationsProvider,
        Provider<DeleteLocationUpdateRequest> deleteLocationUpdateRequestProvider,
        Provider<DeleteProximityAlert> deleteProximityAlertProvider,
        Provider<DeleteProximityAlertEvents> deleteProximityAlertEventsProvider,
        Provider<GetAllProviders> getAllProvider,
        Provider<GetConfig> getConfigProvider,
        Provider<GetDatabaseInformation> getDatabaseInformationProvider,
        Provider<GetLastKnownLocation> getLastKnownLocationProvider,
        Provider<GetLocations> getLocationsProvider,
        Provider<GetLocationUpdateRequests> getLocationUpdateRequestsProvider,
        Provider<GetProvider> getProvider,
        Provider<GetProximityAlertEvents> getProximityAlertEventsProvider,
        Provider<GetProximityAlerts> getProximityAlertsProvider,
        Provider<IsLocationEnabled> isLocationEnabledProvider,
        Provider<IsProviderEnabled> isProviderEnabledProvider,
        Provider<OnLocationStatusChanged> onLocationStatusChangedProvider,
        Provider<OnLocationUpdate> onLocationUpdateProvider,
        Provider<OnProviderEnabled> onProviderChangedProvider,
        Provider<OnProximityAlert> onProximityAlertProvider,
        Provider<Pause> pauseProvider,
        Provider<ReadDiagnostics> readDiagnosticsProvider,
        Provider<RequestLocationUpdate> requestLocationUpdateProvider,
        Provider<RequestProximityAlert> requestProximityAlertProvider,
        Provider<Resume> resumeProvider,
        Provider<SetConfig> setConfigProvider,
        Provider<StartForeground> startForegroundProvider,
        Provider<StopForeground> stopForegroundProvider
    ) {
        Map<String, Provider<? extends LocationAction>> actionProviders = new HashMap<>();
        
        actionProviders.put(Compact.ACTION, compactProvider);
        actionProviders.put(Delete.ACTION, deleteProvider);
        actionProviders.put(DeleteAll.ACTION, deleteAllProvider);
        actionProviders.put(DeleteLocations.ACTION, deleteLocationsProvider);
        actionProviders.put(DeleteLocationUpdateRequest.ACTION,
                            deleteLocationUpdateRequestProvider);
        actionProviders.put(DeleteProximityAlert.ACTION, deleteProximityAlertProvider);
        actionProviders.put(DeleteProximityAlertEvents.ACTION, deleteProximityAlertEventsProvider);
        actionProviders.put(GetAllProviders.ACTION, getAllProvider);
        actionProviders.put(GetConfig.ACTION, getConfigProvider);
        actionProviders.put(GetDatabaseInformation.ACTION, getDatabaseInformationProvider);
        actionProviders.put(GetLastKnownLocation.ACTION, getLastKnownLocationProvider);
        actionProviders.put(GetLocations.ACTION, getLocationsProvider);
        actionProviders.put(GetLocationUpdateRequests.ACTION, getLocationUpdateRequestsProvider);
        actionProviders.put(GetProvider.ACTION, getProvider);
        actionProviders.put(GetProximityAlertEvents.ACTION, getProximityAlertEventsProvider);
        actionProviders.put(GetProximityAlerts.ACTION, getProximityAlertsProvider);
        actionProviders.put(IsLocationEnabled.ACTION, isLocationEnabledProvider);
        actionProviders.put(IsProviderEnabled.ACTION, isProviderEnabledProvider);
        actionProviders.put(OnLocationStatusChanged.ACTION, onLocationStatusChangedProvider);
        actionProviders.put(OnLocationUpdate.ACTION, onLocationUpdateProvider);
        actionProviders.put(OnProviderEnabled.ACTION, onProviderChangedProvider);
        actionProviders.put(OnProximityAlert.ACTION, onProximityAlertProvider);
        actionProviders.put(Pause.ACTION, pauseProvider);
        actionProviders.put(ReadDiagnostics.ACTION,readDiagnosticsProvider);
        actionProviders.put(RequestLocationUpdate.ACTION, requestLocationUpdateProvider);
        actionProviders.put(RequestProximityAlert.ACTION, requestProximityAlertProvider);
        actionProviders.put(Resume.ACTION, resumeProvider);
        actionProviders.put(SetConfig.ACTION, setConfigProvider);
        actionProviders.put(StartForeground.ACTION, startForegroundProvider);
        actionProviders.put(StopForeground.ACTION, stopForegroundProvider);
        
        return actionProviders;
    }
}
