package com.dff.cordova.plugin.location.plugin.dagger;

import android.annotation.SuppressLint;
import android.content.Context;

import com.dff.cordova.plugin.location.core.dagger.LocationCoreDaggerManager;
import com.dff.cordova.plugin.location.plugin.LocationPlugin;
import com.dff.cordova.plugin.location.plugin.dagger.components.DaggerLocationPluginComponent;
import com.dff.cordova.plugin.location.plugin.dagger.components.LocationPluginComponent;
import com.dff.cordova.plugin.shared.dagger.AbstractDaggerManager;

/**
 * Dagger manager for location core component.
 */
public class LocationPluginDaggerManager extends AbstractDaggerManager {
    @SuppressLint("StaticFieldLeak")
    private static LocationPluginDaggerManager instance;

    private LocationPluginComponent locationPluginComponent;
    private Context context;

    /**
     * Private to construct singleton.
     */
    private LocationPluginDaggerManager() {}

    /**
     * Get the one and only {@link LocationPluginDaggerManager}.
     *
     * @return The one and only manager
     */
    public static synchronized LocationPluginDaggerManager getInstance() {
        if (instance == null) {
            instance = new LocationPluginDaggerManager();
        }

        return instance;
    }
    

    /**
     * Set application context.
     *
     * @param context Application context
     * @return Self to chain calls.
     */
    public synchronized LocationPluginDaggerManager context(Context context) {
        this.context = context;

        return this;
    }
    
    /**
     * Inject dependencies into {@link LocationPlugin}.
     *
     * @param plugin ping plugin
     */
    public void inject(LocationPlugin plugin) {
        getComponent().inject(plugin);
    }

    /**
     * Get @{@link LocationPluginComponent} as singleton with given context.
     *
     * @return location core component
     */
    synchronized LocationPluginComponent getComponent() {
        if (locationPluginComponent == null && context != null) {
            locationPluginComponent = DaggerLocationPluginComponent
                .builder()
                .locationCoreComponent(
                    LocationCoreDaggerManager
                        .getInstance()
                        .context(context)
                        .getComponent()
                )
                .build();
        }

        return locationPluginComponent;
    }

    /**
     * Destroy component and context.
     */
    @Override
    public void onDestroy() {
        synchronized (this) {
            locationPluginComponent = null;
            context = null;
        }
    }
}
