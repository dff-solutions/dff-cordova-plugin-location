package com.dff.cordova.plugin.location.services;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import com.dff.cordova.plugin.location.configurations.JSActions;
import com.dff.cordova.plugin.location.dagger.DaggerManager;
import com.dff.cordova.plugin.location.dagger.annotations.LocationServiceHandlerThread;
import com.dff.cordova.plugin.location.dagger.annotations.LocationServiceMessenger;
import com.dff.cordova.plugin.location.events.OnLocationServiceBindEvent;
import com.dff.cordova.plugin.location.events.OnNewGoodLocation;
import com.dff.cordova.plugin.location.handlers.LocationServiceHandler;
import com.dff.cordova.plugin.location.utilities.helpers.CrashHelper;
import com.dff.cordova.plugin.location.utilities.helpers.FileHelper;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * Location Service performs a long running operation in order to the location of the device on change.
 *
 * @author Anthony Nahas
 * @version 2.0
 * @since 28.11.2016
 */
public class LocationService extends Service {

    private static final String TAG = "LocationService";

    @Inject
    @LocationServiceHandlerThread
    HandlerThread mHandlerThread;

    @Inject
    LocationServiceHandler mLocationServiceHandler;

    @Inject
    @LocationServiceMessenger
    Messenger mMessenger;

    @Inject
    EventBus mEventBus;

    @Inject
    CrashHelper mCrashHelper;

    @Inject
    FileHelper mFileHelper;

    @Inject
    JSActions mJsActions;

    @Inject
    PreferencesHelper mPreferencesHelper;

    private int count;

    /**
     * Initialization of properties and handling the location on app crash.
     */
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        DaggerManager
            .getInstance()
            .in(getApplication())
            .inject(this);
        super.onCreate();
        mEventBus.register(this);
        Thread.setDefaultUncaughtExceptionHandler(mCrashHelper);
    }

    /**
     * Recommended in order to restart the service after crash or
     * similar..
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(LocationService.this, "onStartCommand()", Toast.LENGTH_SHORT).show();
        //testService(100);
        Log.d(TAG, "onStartCommand()");
        Log.d(TAG, "can be cleared = " + mPreferencesHelper.getCanLocationBeCleared());
        Log.d(TAG, "is service started --> " + mPreferencesHelper.isServiceStarted());
        Log.d(TAG, "is location manager listening --> " + LocationServiceHandler.isListening);
        if (mPreferencesHelper.isServiceStarted() && !LocationServiceHandler.isListening) {
            startService(new Intent(this, PendingLocationsIntentService.class)
                .setAction(mJsActions.restore_pending_locations));
            initializeLocationManagerAgain();
            Log.d(TAG, "init again");
        }
        return super.onStartCommand(intent, flags, startId); //start sticky
    }

    /**
     * omBind:
     * Bind the messenger with the service.
     *
     * @param intent The intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        mEventBus.post(new OnLocationServiceBindEvent());
        return mMessenger.getBinder();
    }

    /**
     * Quite safely the handler thread.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
        mEventBus.unregister(this);
        mHandlerThread.quitSafely();
        //Toast.makeText(LocationService.this, "onDestroy()", Toast.LENGTH_SHORT).show();  //remove in production
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "onLowMemory()");
        super.onLowMemory();
        //Toast.makeText(LocationService.this, "onLowMemory()", Toast.LENGTH_SHORT).show(); //remove in production
    }

    @Override
    public void onTrimMemory(int level) {
        Log.d(TAG, "onTrimMemory()");
        super.onTrimMemory(level);
        //Toast.makeText(LocationService.this, "onTrimMemory()", Toast.LENGTH_SHORT).show(); //remove in production
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind()");
        //Toast.makeText(LocationService.this, "onUnbind()", Toast.LENGTH_SHORT).show(); //remove in production
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind()");
        super.onRebind(intent);
        //Toast.makeText(LocationService.this, "onRebind()", Toast.LENGTH_SHORT).show(); //remove in production
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved()");
        super.onTaskRemoved(rootIntent);  //remove in production
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnNewGoodLocation event) {
        // TODO: 07.07.2017 add location to list
    }

    private void initializeLocationManagerAgain() {
        mPreferencesHelper.restoreProperties();
        mPreferencesHelper.setIsServiceStarted(mLocationServiceHandler.initializeLocationManagerOnRespawn());
    }

    private void testService(int max) {
        while (count < max) {
            Toast.makeText(LocationService.this, "Test within " + count + " counts!", Toast.LENGTH_SHORT).show();
            count++;
        }
    }
}
