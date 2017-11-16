package com.dff.cordova.plugin.location.dagger.modules;

import dagger.Module;
import org.apache.cordova.CordovaInterface;

/**
 * Created by anahas on 16.06.2017
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 16.06.17
 */
@Module
public class CordovaModule { // todo --> ref --> remove

    private CordovaInterface mCordovaInterface;

    public CordovaModule(CordovaInterface mCordovaInterface) {
        this.mCordovaInterface = mCordovaInterface;
    }

}
