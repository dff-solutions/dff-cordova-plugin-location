package com.dff.cordova.plugin.location.actions;

import android.content.Context;

import com.dff.cordova.plugin.location.abstracts.Action;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;

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
public class StopLocationServiceAction extends Action {

    private Context mContext;

    @Inject
    public StopLocationServiceAction(
        @ApplicationContext Context mContext
    ) {
        this.mContext = mContext;
    }

    @Override
    public Action execute() {
        return null;
    }
}
