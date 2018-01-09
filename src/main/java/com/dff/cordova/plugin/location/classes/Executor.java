package com.dff.cordova.plugin.location.classes;


import android.Manifest;
import android.content.pm.PackageManager;
import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.dagger2.abstracts.Action;
import com.dff.cordova.plugin.dagger2.annotations.Shared;
import com.dff.cordova.plugin.location.events.OnRequestPermissionResult;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;

/**
 * Class to execute incoming actions from JS.
 *
 * @author Anthony Nahas
 * @version 9.0.0-rc4
 * @since 15.12.2016
 */
@Singleton
public class Executor {

    private static final String TAG = "Executor";

    private final int mRequestCode = 3091;
    private final String[] mLocationPermissions =
        {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        };
    private CordovaInterface mCordova;
    private EventBus mEventBus;
    private ArrayList<Action> mPostPoneActions;

    @Inject
    public Executor(@Shared CordovaInterface mCordova, EventBus mEventBus) {
        this.mCordova = mCordova;
        this.mEventBus = mEventBus;
        mPostPoneActions = new ArrayList<>();
        mEventBus.register(this);
    }

    /**
     * request permissions if they are not granted before executing any action
     */
    public <T extends Action> void execute(T action) {
            action.execute();
    }

    /**
     * request permissions if they are not granted before executing any action
     */
    public <T extends Action> void execute(CordovaPlugin cordovaPlugin, T action) {
        if (mCordova.hasPermission(mLocationPermissions[0])) {
            action.execute();
        } else {
            mPostPoneActions.add(action);
            mCordova.requestPermissions(cordovaPlugin, mRequestCode, mLocationPermissions);
        }
    }

    private void runPostPoneActions() {
        if (mPostPoneActions.size() > 0) {
            for (Action action : mPostPoneActions) {
                action.execute();
            }
        }
    }

    private void blockPostPoneActions() {
        if (mPostPoneActions.size() > 0) {
            for (Action action : mPostPoneActions) {
                action.getCallbackContext().error("PERMISSION DENIED for " + mLocationPermissions);
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnRequestPermissionResult event) {
        for (int result : event.getGrantResults()) {
            if (result == PackageManager.PERMISSION_DENIED && event.getRequestCode() == mRequestCode) {
                CordovaPluginLog.e(TAG, "PERMISSION DENIED @code " + event.getRequestCode());
                blockPostPoneActions();
                return;
            }
        }
        runPostPoneActions();
    }

}
