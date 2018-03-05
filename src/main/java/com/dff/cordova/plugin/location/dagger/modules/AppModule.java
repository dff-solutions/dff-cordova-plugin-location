package com.dff.cordova.plugin.location.dagger.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import com.dff.cordova.plugin.dagger2.annotations.ApplicationContext;
import com.dff.cordova.plugin.dagger2.annotations.DefaultUncaughException;
import com.dff.cordova.plugin.dagger2.annotations.Private;
import com.dff.cordova.plugin.dagger2.annotations.Shared;
import com.dff.cordova.plugin.location.classes.GLocationManager;
import com.dff.cordova.plugin.location.resources.Res;
import com.dff.cordova.plugin.location.resources.Resources;
import com.dff.cordova.plugin.location.utilities.helpers.LocationHelper;
import dagger.Module;
import dagger.Provides;
import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

/**
 * The @Module annotation tells Dagger that the AppModule classes will provide dependencies for a part
 * of the mApp. It is normal to have multiple Dagger modules in a project, and it is typical
 * for one of them to provide app-wide dependencies.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 12.06.17
 */
@Module
public class AppModule {

    private Res mRes;
    private Application mApp;
    private GLocationManager mGLocationManager;

    public AppModule(Application app) {
        this.mApp = app;
    }

    public Application getApp() {
        return mApp;
    }

    @Provides
    @ApplicationContext
    public Context provideContext() {
        return mApp;
    }

    @Provides
    public Application provideApplication() {
        return mApp;
    }

    @Provides
    @Singleton
    public EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @Singleton
    @DefaultUncaughException
    public Thread.UncaughtExceptionHandler provideDefaultThreadUncaughtExceptionHandler() {
        return Thread.getDefaultUncaughtExceptionHandler();
    }

    // Dagger will only look for methods annotated with @Provides
    // Application reference must come from AppModule.classes
    @Provides
    @Singleton
    @Shared
    public SharedPreferences providesSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mApp);
    }

    @Provides
    @Singleton
    @Private
    public SharedPreferences providePrivateSharedPreferences() {
        return mApp.getSharedPreferences(Resources.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    public Handler provideHandler() {
        return new Handler();
    }

    @Provides
    @Singleton
    @Shared
    public Res provideRes(LocationHelper locationHelper) {
        if (mRes != null) {
            return mRes;
        }

        mRes = new Res(locationHelper);
        return mRes;
    }

    @Provides
    @Singleton
    @Shared
    public GLocationManager provideGLocationManager(@ApplicationContext Context mContext,
                                                    @Shared Res mRes,
                                                    EventBus mEventBus) {
        if (mGLocationManager != null) {
            return mGLocationManager;
        }

        mGLocationManager = new GLocationManager(mContext, mRes, mEventBus);
        return mGLocationManager;
    }
}
