package com.dff.cordova.plugin.location.dagger;

import android.app.Application;
import android.app.Service;

import com.dff.cordova.plugin.location.LocationPlugin;
import com.dff.cordova.plugin.location.dagger.components.DaggerPluginComponent;
import com.dff.cordova.plugin.location.dagger.components.DaggerServiceComponent;
import com.dff.cordova.plugin.location.dagger.components.PluginComponent;
import com.dff.cordova.plugin.location.dagger.components.ServiceComponent;
import com.dff.cordova.plugin.location.dagger.modules.AppModule;
import com.dff.cordova.plugin.location.dagger.modules.CordovaModule;
import com.dff.cordova.plugin.location.dagger.modules.PluginModule;
import com.dff.cordova.plugin.location.dagger.modules.ServiceModule;
import com.dff.cordova.plugin.location.services.LocationService;
import com.dff.cordova.plugin.location.services.PendingLocationsIntentService;

import org.apache.cordova.CordovaInterface;

/**
 * Created by anahas on 22.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 22.06.17
 */

public class DaggerManager {

    private static DaggerManager mDaggerManager;

    private PluginComponent mPluginComponent;
    private ServiceComponent mServiceComponent;

    private AppModule mAppModule;
    private CordovaModule mCordovaModule;
    private PluginModule mPluginModule;

    private Application mApplication;
    private CordovaInterface mCordovaInterface;


    public static synchronized DaggerManager getInstance() {
        if (mDaggerManager == null) {
            mDaggerManager = new DaggerManager();
        }
        return mDaggerManager;
    }

    public DaggerManager in(Application mApplication) {
        this.mApplication = mApplication;
        if (mAppModule == null) {
            mAppModule = mApplication != null ? new AppModule(mApplication) : null;
        }
        return this;
    }

    public DaggerManager and(CordovaInterface mCordovaInterface) {
        this.mCordovaInterface = mCordovaInterface;
        if (mCordovaModule == null) {
            mCordovaModule = mCordovaInterface != null ? new CordovaModule(mCordovaInterface) : null;
            mPluginModule = mCordovaInterface != null ? new PluginModule() : null;
        }
        return this;
    }


    public void inject(LocationPlugin locationPlugin) {
        if (mPluginComponent == null) {
            mPluginComponent = DaggerPluginComponent
                .builder()
                .appModule(mAppModule)
                .cordovaModule(mCordovaModule)
                .pluginModule(mPluginModule)
                .build();
        }
        mPluginComponent.inject(locationPlugin);
    }

    public <T extends Service> void inject(T service) {
        if (mServiceComponent == null) {

            mServiceComponent = DaggerServiceComponent
                .builder()
                .appModule(mAppModule)
                .serviceModule(new ServiceModule())
                .build();
        }

        if (service instanceof LocationService) {
            mServiceComponent.inject((LocationService) service);
        } else if (service instanceof PendingLocationsIntentService) {
            mServiceComponent.inject((PendingLocationsIntentService) service);
        }
    }
}
