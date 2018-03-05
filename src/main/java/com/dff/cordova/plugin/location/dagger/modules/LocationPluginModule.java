package com.dff.cordova.plugin.location.dagger.modules;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmConfiguration;

/**
 * Shared DI module for the location plugins
 */
@Module
public class LocationPluginModule {

    @Provides
    public RealmConfiguration provideRealmConfiguration(){
        return new RealmConfiguration.Builder()
            .name("locations.realm")
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build();
    }
}
