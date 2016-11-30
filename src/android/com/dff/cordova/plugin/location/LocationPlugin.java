package com.dff.cordova.plugin.location;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import com.dff.cordova.plugin.common.service.CommonServicePlugin;
import com.dff.cordova.plugin.common.service.ServiceHandler;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by anahas on 28.11.2016.
 *
 * @author Anthony Nahas
 * @version 0.1
 * @since 28.11.2016
 */
public class LocationPlugin extends CommonServicePlugin {

    private static final String TAG = "LocationPlugin";
    private static final String ACTION_START_SERVICE = "location.action.START_SERVICE";
    private static final String ACTION_STOP_SERVICE = "location.action.STOP_SERVICE";
    private static final String ACTION_GET_LOCATION = "location.action.GET_LOCATION";
    private Context mContext;
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
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action != null) {
            Log.d(TAG, "Action = " + action);
            if (action.equals(ACTION_START_SERVICE)) {
                mContext.startService(new Intent(mContext, LocationService.class));
                //mContext.bindService(new Intent)
                return true;
            } else if (action.equals(ACTION_STOP_SERVICE)) {
                mContext.stopService(new Intent(mContext, LocationService.class));
                return true;
            } else if (action.equals(ACTION_GET_LOCATION)) {
                Message msg = Message.obtain(null, LocationServiceHandler.WHAT_A);
                msg.replyTo = new Messenger(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.d(TAG, msg.toString());
                    }
                });
                try {
                    this.mServiceHandler.getService().send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return super.execute(action, args, callbackContext);
    }
}
