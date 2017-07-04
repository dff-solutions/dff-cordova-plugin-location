package com.dff.cordova.plugin.location.configurations;

import com.dff.cordova.plugin.location.LocationPluginTest;
import com.dff.cordova.plugin.location.actions.index.IndexActions;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;


/**
 * Created by anahas on 03.07.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 03.07.2017
 */
public class ConfigTest extends LocationPluginTest {

    @Mock
    private IndexActions mIndexActions;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        System.out.println("setup config test");
    }

    @Test
    public void test() {
        System.out.println("setup config test");
    }
}
