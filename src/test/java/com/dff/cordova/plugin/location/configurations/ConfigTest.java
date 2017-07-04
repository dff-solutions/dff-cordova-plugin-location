package com.dff.cordova.plugin.location.configurations;

import com.dff.cordova.plugin.location.LocationPluginTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.inject.Inject;

import static junit.framework.Assert.assertNotNull;


/**
 * Created by anahas on 03.07.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 03.07.2017
 */
public class ConfigTest extends LocationPluginTest {

    @Inject
    JSActions mJsActions;

    @Inject
    ActionsManager mActionsManager;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    public ConfigTest() {
        super();
        Dagger.inject(this);
    }

    @Before
    public void setUp() {
        System.out.println("setup config test");
    }

    @Test
    public void checkAllJsAction() {
        System.out.println("setup config test");
        for (String action : mJsActions.all) {
            print(action);
            assertNotNull(mActionsManager.hash(action));
        }
    }

}
