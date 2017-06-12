package com.dff.cordova.plugin.location.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * The @Module annotation tells Dagger that the AppModule class will provide dependencies for a part
 * of the application. It is normal to have multiple Dagger modules in a project, and it is typical
 * for one of them to provide app-wide dependencies.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 12.06.17
 */

@Module
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    // Application reference must come from AppModule.class
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}
