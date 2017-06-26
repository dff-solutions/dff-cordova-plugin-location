package com.dff.cordova.plugin.location.classes;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.*;

import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.common.service.ServiceHandler;
import com.dff.cordova.plugin.location.actions.Action;
import com.dff.cordova.plugin.location.actions.RestoreAction;
import com.dff.cordova.plugin.location.broadcasts.StandStillReceiver;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.resources.Res;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;

/**
 * Class to execute incoming actions from JS.
 *
 * @author Anthony Nahas
 * @version 8.0.1
 * @since 15.12.2016
 */
@Singleton
@Module
public class Executor {

    private static final String TAG = "Executor";


    private Context mContext;
    private PreferencesHelper mPreferencesHelper;


    //Actions
    private RestoreAction mRestoreAction;

    @Inject
    public Executor
        (@ApplicationContext Context mContext,
         PreferencesHelper mPreferencesHelper) {

        this.mContext = mContext;
        this.mPreferencesHelper = mPreferencesHelper;
    }


    public <T extends Action> void execute(T action) {
        action.execute();
    }


    private void sendMessage(ServiceHandler serviceHandler, Message msg, CallbackContext callbackContext) {
        try {
            Messenger messenger = serviceHandler.getService();
            if (messenger != null) {
                messenger.send(msg);
            }
        } catch (RemoteException | NullPointerException e) {
            CordovaPluginLog.e(TAG, "Error: ", e);
            callbackContext.error("Error while sending a message within the location service: " + e);
        }
    }
}
