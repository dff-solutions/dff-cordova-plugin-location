package com.dff.cordova.plugin.location.plugin.dagger.components;

import android.content.Context;

import com.dff.cordova.plugin.location.plugin.action.LocationAction;
import com.dff.cordova.plugin.location.plugin.dagger.annotations.LocationPluginScope;
import com.dff.cordova.plugin.location.plugin.dagger.module.LocationPluginModule;
import com.dff.cordova.plugin.location.plugin.dagger.modules.TestLocationPluginModule;
import com.dff.cordova.plugin.shared.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.shared.dagger.annotations.PluginPermissions;
import com.dff.cordova.plugin.shared.dagger.annotations.PreferencesName;

import java.util.Map;

import javax.inject.Provider;

import dagger.BindsInstance;
import dagger.Component;

@LocationPluginScope
@Component(
    modules = {
        TestLocationPluginModule.class,
        LocationPluginModule.class
    }
)
public interface TestLocationPluginComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder applicationContext(@ApplicationContext Context context);
        
        @BindsInstance
        Builder pluginPermissions(@PluginPermissions String[] permissions);
    
        @BindsInstance
        Builder preferencesName(@PreferencesName String name);
        
        TestLocationPluginComponent.Builder testLocationPluginModule(TestLocationPluginModule module);
        
        TestLocationPluginComponent build();
    }
    
    Map<String, Provider<? extends LocationAction>> provideActionProviders();
    
}
