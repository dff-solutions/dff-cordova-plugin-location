package com.dff.cordova.plugin.location.plugin.dagger;

import android.content.Context;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
class LocationPluginDaggerManagerUnitTest {
    @Mock
    private Context context;

    @Test
    void shouldGetInstance() {
        assertNotNull(LocationPluginDaggerManager.getInstance());
    }

    @Test
    void shouldCreateSingleton() {
        assertSame(
            LocationPluginDaggerManager.getInstance(),
            LocationPluginDaggerManager.getInstance()
        );
    }

    @Test
    void getComponent_shouldCheckContext() {
        LocationPluginDaggerManager daggerManager = LocationPluginDaggerManager.getInstance();
        assertNull(daggerManager.getComponent());

        daggerManager.context(context);

        assertNotNull(daggerManager.getComponent());

        assertSame(
            daggerManager.getComponent(),
            daggerManager.getComponent()
        );
    }
}
