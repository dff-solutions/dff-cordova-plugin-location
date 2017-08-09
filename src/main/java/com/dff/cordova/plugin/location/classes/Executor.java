package com.dff.cordova.plugin.location.classes;

import android.content.Context;

import com.dff.cordova.plugin.common.action.Action;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class to execute incoming actions from JS.
 *
 * @author Anthony Nahas
 * @version 8.0.1
 * @since 15.12.2016
 */
@Singleton
public class Executor {

    private static final String TAG = "Executor";


    @Inject
    public Executor
        (@ApplicationContext Context mContext,
         PreferencesHelper mPreferencesHelper) {
    }

    public <T extends Action> void execute(T action) {
        action.execute();
    }
}
