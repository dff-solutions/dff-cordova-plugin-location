package com.dff.cordova.plugin.location.actions;

import android.content.Context;
import android.content.Intent;

import com.dff.cordova.plugin.location.interfaces.Executable;
import com.dff.cordova.plugin.location.services.LocationService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by anahas on 16.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 16.06.17
 */
@Singleton
public class StartLocationService implements Executable {

    // TODO: 16.06.2017 @Injects
    private Context mContext;
    //RequestHandler

    @Inject
    public StartLocationService(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void execute() {
        // TODO: 16.06.2017
        mContext.startService(new Intent(mContext, LocationService.class));
    }
}
