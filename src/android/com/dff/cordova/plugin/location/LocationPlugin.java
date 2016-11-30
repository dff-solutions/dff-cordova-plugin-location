package com.dff.cordova.plugin.location;

import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.util.Log;
import com.dff.cordova.plugin.common.service.CommonServicePlugin;
import com.dff.cordova.plugin.common.service.ServiceHandler;
import com.dff.cordova.plugin.location.handlers.LocationRequestHandler;
import com.dff.cordova.plugin.location.resources.LocationResources;
import com.dff.cordova.plugin.location.services.LocationService;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by anahas on 28.11.2016.
 *
 * @author Anthony Nahas
 * @version 0.4
 * @since 28.11.2016
 */
public class LocationPlugin extends CommonServicePlugin {

    private static final String TAG = "LocationPlugin";
    private static final String ACTION_START_SERVICE = "location.action.START_SERVICE";
    private static final String ACTION_STOP_SERVICE = "location.action.STOP_SERVICE";
    private static final String ACTION_GET_LOCATION = "location.action.GET_LOCATION";
    private Context mContext;
    private HandlerThread mHandlerThread;
    private ServiceHandler mServiceHandler;

    public LocationPlugin() {
        super(TAG);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void pluginInitialize() {
        mContext = cordova.getActivity().getApplicationContext();
        mServiceHandler = new ServiceHandler(this.cordova, LocationService.class);
        super.pluginInitialize(mServiceHandler);
        mServiceHandler.bindService();
        mHandlerThread = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
    }

    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action != null) {
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Action = " + action);
                    if (action.equals(ACTION_START_SERVICE)) {
                        mContext.startService(new Intent(mContext, LocationService.class));
                        //mContext.bindService(new Intent)
                    } else if (action.equals(ACTION_STOP_SERVICE)) {
                        mContext.stopService(new Intent(mContext, LocationService.class));
                    } else if (action.equals(ACTION_GET_LOCATION)) {
                        Message msg = Message.obtain(null, LocationResources.ACTION_GET_LOCATION);
                        //new LocationRequestHandler(callbackContext
                        LocationRequestHandler handler = new LocationRequestHandler(mHandlerThread.getLooper(), callbackContext);
                        msg.replyTo = new Messenger(handler);
                        try {
                            mServiceHandler.getService().send(msg);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            return true;
        }
        return super.execute(action, args, callbackContext);
    }
}
