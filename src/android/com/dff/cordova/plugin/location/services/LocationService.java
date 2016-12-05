package com.dff.cordova.plugin.location.services;

import android.app.Service;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Messenger;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import com.dff.cordova.plugin.location.handlers.LocationServiceHandler;
import com.dff.cordova.plugin.location.resources.LocationResources;

/**
 * Created by anahas on 28.11.2016.
 *
 * @author Anthony Nahas
 * @version 0.9
 * @since 28.11.2016
 */
public class LocationService extends Service {

    private static final String Tag = "LocationService";

    private HandlerThread mHandlerThread;
    private LocationServiceHandler mLocationServiceHandler;
    private Messenger mMessenger;
    private int count = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Tag, "onCreate()");
        Toast.makeText(LocationService.this, "onCreate()", Toast.LENGTH_SHORT).show();
        mHandlerThread = new HandlerThread(Tag, Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mLocationServiceHandler = new LocationServiceHandler(mHandlerThread.getLooper(), this);
        mMessenger = new Messenger(mLocationServiceHandler);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Tag, "onStartCommand()");
        Toast.makeText(LocationService.this, "onStartCommand()", Toast.LENGTH_SHORT).show();
        testService(45);
        Intent pendingLocationsIntentService = new Intent(this, PendingLocationsIntentService.class);
        LocationResources.addLocationToList("Test");
        pendingLocationsIntentService.setAction(LocationResources.ACTION_INTENT_STORE_PENDING_LOCATIONS);
        this.startService(pendingLocationsIntentService);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Tag, "onBind()");
        Toast.makeText(LocationService.this, "onBind()", Toast.LENGTH_SHORT).show();
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        Log.d(Tag, "onDestroy()");
        super.onDestroy();
        mHandlerThread.quitSafely();
        Toast.makeText(LocationService.this, "onDestroy()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLowMemory() {
        Log.d(Tag, "onLowMemory()");
        super.onLowMemory();
        Toast.makeText(LocationService.this, "onLowMemory()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTrimMemory(int level) {
        Log.d(Tag, "onTrimMemory()");
        super.onTrimMemory(level);
        Toast.makeText(LocationService.this, "onTrimMemory()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(Tag, "onUnbind()");
        Toast.makeText(LocationService.this, "onUnbind()", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(Tag, "onRebind()");
        super.onRebind(intent);
        Toast.makeText(LocationService.this, "onRebind()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(Tag, "onTaskRemoved()");
        super.onTaskRemoved(rootIntent);
    }

    private void testService(int max) {
        while (count < max) {
            Toast.makeText(LocationService.this, "Test within " + count + " counts!", Toast.LENGTH_SHORT).show();
            count++;
        }
    }
}
