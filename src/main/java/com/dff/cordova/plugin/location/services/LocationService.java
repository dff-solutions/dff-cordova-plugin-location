package com.dff.cordova.plugin.location.services;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import com.dff.cordova.plugin.dagger2.annotations.Shared;
import com.dff.cordova.plugin.location.classes.GLocationManager;
import com.dff.cordova.plugin.location.configurations.JSActions;
import com.dff.cordova.plugin.location.dagger.DaggerManager;
import com.dff.cordova.plugin.location.events.OnNewGoodLocation;
import com.dff.cordova.plugin.location.events.OnStartLocationService;
import com.dff.cordova.plugin.location.events.OnStopLocationService;
import com.dff.cordova.plugin.location.utilities.helpers.CrashHelper;
import com.dff.cordova.plugin.location.utilities.helpers.FileHelper;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * Location Service performs a long running operation in order to the location of the device on change.
 *
 * @author Anthony Nahas
 * @version 9.1.0-beta.4
 * @since 28.11.2016
 */
public class LocationService extends Service {

    private static final String TAG = "LocationService";

    @Inject
    @Shared
    GLocationManager mGLocationManager;

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

    private Realm realm;

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
        mPreferencesHelper.setIsServiceStarted(mGLocationManager.init());
        Thread.setDefaultUncaughtExceptionHandler(mCrashHelper);

        // Initialize Realm
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
            .name("locations.realm")
            .schemaVersion(0)
//            .migration(migration)
            .build();
        realm = Realm.getInstance(config);
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
        Log.d(TAG, "is location manager listening --> " + mGLocationManager.isListening());
        if (!mPreferencesHelper.isServiceStarted() || !mGLocationManager.isListening()) {
            startService(new Intent(this, PendingLocationsIntentService.class)
                .setAction(mJsActions.restore_pending_locations));
            initializeLocationManager();
            Log.d(TAG, "init again");
        }
        mEventBus.post(new OnStartLocationService());
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
        return null;
    }

    /**
     * Quite safely the handler thread.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        mGLocationManager.setListening(false);
        mEventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "onLowMemory()");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Log.d(TAG, "onTrimMemory()");
        super.onTrimMemory(level);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved()");
        super.onTaskRemoved(rootIntent);  //remove in production
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnNewGoodLocation event) {
        // TODO: 07.07.2017 add location to list
        realm.executeTransactionAsync(realm -> realm.insert(event.getLocation()),
            () -> {
                // Transaction was a success.
                Log.d(TAG, "Transaction was a success for --> " + event.getLocation());
            }, error -> {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "Transaction failed and was automatically canceled for --> " + event.getLocation() + " Error: " +
                    error);
            });
    }

    @Subscribe
    public void onMessageEvent(OnStopLocationService event) {
        mPreferencesHelper.setIsServiceStarted(false);
        mGLocationManager.removeUpdates();
        event.getCallbackContext().success();
        stopSelf();
    }

    /**
     * On uncaught exception - the process will be respawn
     */
    private void initializeLocationManager() {
        mPreferencesHelper.restoreProperties();
        mPreferencesHelper.setIsServiceStarted(mGLocationManager.init());
    }
}

