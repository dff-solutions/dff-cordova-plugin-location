package com.dff.cordova.plugin.location.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.dff.cordova.plugin.location.classes.GLocationManager;
import com.dff.cordova.plugin.location.configurations.JSActions;
import com.dff.cordova.plugin.location.dagger.DaggerManager;
import com.dff.cordova.plugin.location.dagger.annotations.Shared;
import com.dff.cordova.plugin.location.events.OnNewGoodLocation;
import com.dff.cordova.plugin.location.events.OnStartLocationService;
import com.dff.cordova.plugin.location.events.OnStopLocationService;
import com.dff.cordova.plugin.location.utilities.helpers.CrashHelper;
import com.dff.cordova.plugin.location.utilities.helpers.FileHelper;
import com.dff.cordova.plugin.location.utilities.helpers.PreferencesHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import javax.inject.Inject;

import static android.os.PowerManager.PARTIAL_WAKE_LOCK;

/**
 * Location Service performs a long running operation in order to the location of the device on change.
 *
 * @author Anthony Nahas
 * @version 9.1.0-beta.4
 * @since 28.11.2016
 */
public class LocationService extends Service {

    private static final String TAG = "LocationService";

    // Fixed ID for the 'foreground' notification
    public static final int NOTIFICATION_ID = -574543954;

    // Default title of the background notification
    private static final String NOTIFICATION_TITLE = "App is running in background";

    // Default icon of the background notification
    private static final String NOTIFICATION_ICON = "icon";

    // Partial wake lock to prevent the app from going to sleep when locked
    private PowerManager.WakeLock wakeLock;


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
        if (com.dff.cordova.plugin.location.resources.Resources.LOCATION_FOREGROUND_MODE) {
            keepAwake();
        }
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
        if (com.dff.cordova.plugin.location.resources.Resources.LOCATION_FOREGROUND_MODE) {
            sleepWell();
        }
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
    }

    @Subscribe
    public void onMessageEvent(OnStopLocationService event) {
        mPreferencesHelper.setIsServiceStarted(false);
        mGLocationManager.removeUpdates();
        event.getCallbackContext().success();
        if (com.dff.cordova.plugin.location.resources.Resources.LOCATION_FOREGROUND_MODE) {
            sleepWell();
        }
        stopSelf();
    }

    /**
     * On uncaught exception - the process will be respawn
     */
    private void initializeLocationManager() {
        mPreferencesHelper.restoreProperties();
        mPreferencesHelper.setIsServiceStarted(mGLocationManager.init());
    }

    /**
     * Put the service in a foreground state to prevent app from being killed
     * by the OS.
     */
    private void keepAwake() {
        startForeground(NOTIFICATION_ID, makeNotification());


        PowerManager pm = (PowerManager)
                getSystemService(POWER_SERVICE);

        if (pm != null) {
            wakeLock = pm.newWakeLock(
                    PARTIAL_WAKE_LOCK, "BackgroundMode");
        }

        wakeLock.acquire(5000);
    }

    /**
     * Stop background mode.
     */
    private void sleepWell() {
        stopForeground(true);
        getNotificationManager().cancel(NOTIFICATION_ID);
    }


    /**
     * Create a notification as the visible part to be able to put the service
     * in a foreground state.
     */
    private Notification makeNotification() {

        Context context = getApplicationContext();
        String pkgName = context.getPackageName();
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(pkgName);

        Notification.Builder notification = new Notification.Builder(context)
                .setContentTitle("App is running")
                .setSmallIcon(getResources().getIdentifier("ic_local_shipping", "drawable", getPackageName()))
                .setOngoing(true);

//        notification.setPriority(Notification.PRIORITY_MIN);


        setColor(notification);

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(
                    this, NOTIFICATION_ID, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);


            notification.setContentIntent(contentIntent);
        }

        return notification.build();
    }

    /**
     * Update the notification.
     *
     * @param settings The config settings
     */
    protected void updateNotification(JSONObject settings) {
        boolean isSilent = settings.optBoolean("silent", false);

        if (isSilent) {
            stopForeground(true);
            return;
        }

        Notification notification = makeNotification();
        getNotificationManager().notify(NOTIFICATION_ID, notification);
    }

    /**
     * Retrieves the resource ID of the app icon.
     *
     * @param settings A JSON dict containing the icon name.
     */
    private int getIconResId(JSONObject settings) {
        String icon = settings.optString("icon", NOTIFICATION_ICON);

        // cordova-android 6 uses mipmaps
        int resId = getIconResId(icon, "mipmap");

        if (resId == 0) {
            resId = getIconResId(icon, "drawable");
        }

        return resId;
    }

    /**
     * Retrieve resource id of the specified icon.
     *
     * @param icon The name of the icon.
     * @param type The resource type where to look for.
     * @return The resource id or 0 if not found.
     */
    private int getIconResId(String icon, String type) {
        Resources res = getResources();
        String pkgName = getPackageName();

        int resId = res.getIdentifier(icon, type, pkgName);

        if (resId == 0) {
            resId = res.getIdentifier("icon", type, pkgName);
        }

        return resId;
    }

    /**
     * Set notification color if its supported by the SDK.
     *
     * @param notification A Notification.Builder instance
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setColor(Notification.Builder notification) {
        try {
            notification.setColor(Color.GRAY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shared manager for the notification service.
     */
    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
}

