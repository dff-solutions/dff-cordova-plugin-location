package com.dff.cordova.plugin.location.actions.index;

import com.dff.cordova.plugin.location.actions.RestoreAction;
import com.dff.cordova.plugin.location.actions.StartLocationServiceAction;
import com.dff.cordova.plugin.location.actions.StopLocationServiceAction;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by anahas on 19.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 19.06.17
 */
@Singleton
public class IndexActions {

    // 3 Actions
    public RestoreAction mRestoreAction;
    public StartLocationServiceAction mStartLocationServiceAction;
    public StopLocationServiceAction mStopLocationServiceAction;

    @Inject
    public IndexActions(RestoreAction mRestoreAction) {
        this.mRestoreAction = mRestoreAction;
        // TODO: 19.06.2017 update constructor
    }
}
