package com.dff.cordova.plugin.location.actions;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dff.cordova.plugin.location.broadcasts.NewLocationReceiver;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.resources.Res;

import org.json.JSONException;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by anahas on 28.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 28.06.17
 */
@Singleton
public class RegisterLocationListenerAction extends Action {

    private static final String TAG = RegisterLocationListenerAction.class.getSimpleName();

    private Context mContext;
    private NewLocationReceiver mNewLocationReceiver;

    @Inject
    public RegisterLocationListenerAction
        (
            @ApplicationContext Context mContext,
            NewLocationReceiver mNewLocationReceiver
        ) {
        this.mContext = mContext;
        this.mNewLocationReceiver = mNewLocationReceiver;
    }

    @Override
    public void execute() {

        int type = 1;
        try {
            if (args.get(0) != null) {
                type = args.getInt(0);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }

        mNewLocationReceiver.setCallbackContext(callbackContext);
        mNewLocationReceiver.setType(type);

        LocalBroadcastManager.getInstance(mContext).
            registerReceiver(mNewLocationReceiver, new IntentFilter(Res.BROADCAST_ACTION_ON_NEW_LOCATION));
    }
}
