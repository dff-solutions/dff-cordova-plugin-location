package com.dff.cordova.plugin.location.services;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.common.service.ServiceHandler;
import com.dff.cordova.plugin.location.LocationPlugin;
import com.dff.cordova.plugin.location.handlers.LocationServiceHandler;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.utilities.helpers.CrashHelper;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;

/**
 * Location Service performs a long running operation in order to the location of the device on change.
 *
 * @author Anthony Nahas
 * @version 1.2
 * @since 28.11.2016
 */
public class LocationService extends Service {

    private static final String TAG = "LocationService";

    private HandlerThread mHandlerThread;
    private LocationServiceHandler mLocationServiceHandler;
    private Messenger mMessenger;
    private PreferencesHelper mPreferencesHelper;
    private int count;

    /**
     * Initialization of properties and handling the location on app crash.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
        //Toast.makeText(LocationService.this, "onCreate()", Toast.LENGTH_SHORT).show(); //remove in production
        mHandlerThread = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mLocationServiceHandler = new LocationServiceHandler(mHandlerThread.getLooper(), this);
        mMessenger = new Messenger(mLocationServiceHandler);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHelper(this, Thread.getDefaultUncaughtExceptionHandler()));
        mPreferencesHelper = new PreferencesHelper(this);
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
        if (mPreferencesHelper.isServiceStarted() && !LocationServiceHandler.isListening) {
            initializeLocationManagerAgain();
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
        //Toast.makeText(LocationService.this, "onBind()", Toast.LENGTH_SHORT).show();
        return mMessenger.getBinder();
    }

    /**
     * Quite safely the handler thread.
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
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

    private void initializeLocationManagerAgain() {
        Bundle data = new Bundle();
        ServiceHandler serviceHandler = LocationPlugin.getServiceHandler();
        //HandlerThread handlerThread = LocationPlugin.getHandlerThread();
        data.putLong(LocationResources.LOCATION_MIN_TIME_KEY, LocationResources.LOCATION_MIN_TIME);
        data.putFloat(LocationResources.LOCATION_MIN_DISTANCE_KEY, LocationResources.LOCATION_MIN_DISTANCE);
        Message msg = Message.obtain(null, LocationResources.WHAT.START_LOCATION_SERVICE.ordinal());
        msg.setData(data);
        try {
            Messenger messenger = serviceHandler.getService();
            if (messenger != null) {
                messenger.send(msg);
            }
        } catch (RemoteException | NullPointerException e) {
            CordovaPluginLog.e(TAG, "Error: ", e);
        }
        mPreferencesHelper.setIsServiceStarted(false);
    }

    private void testService(int max) {
        while (count < max) {
            Toast.makeText(LocationService.this, "Test within " + count + " counts!", Toast.LENGTH_SHORT).show();
            count++;
        }
    }
}
