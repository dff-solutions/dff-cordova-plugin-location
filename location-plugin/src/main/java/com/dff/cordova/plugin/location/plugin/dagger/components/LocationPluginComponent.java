package com.dff.cordova.plugin.location.plugin.dagger.components;

import com.dff.cordova.plugin.location.core.dagger.components.LocationCoreComponent;
import com.dff.cordova.plugin.location.plugin.LocationPlugin;
import com.dff.cordova.plugin.location.plugin.dagger.annotations.LocationPluginScope;
import com.dff.cordova.plugin.location.plugin.dagger.module.LocationPluginModule;

import dagger.Component;

/**
 * Dagger component for the location plugin.
 */
@Component(modules = LocationPluginModule.class, dependencies = LocationCoreComponent.class)
@LocationPluginScope
public interface LocationPluginComponent {
    
    /**
     * Interface builder.
     */
    @Component.Builder
    interface Builder {
        Builder locationCoreComponent(LocationCoreComponent locationCoreComponent);
        
        LocationPluginComponent build();
    }
    
    void inject(LocationPlugin plugin);
}
