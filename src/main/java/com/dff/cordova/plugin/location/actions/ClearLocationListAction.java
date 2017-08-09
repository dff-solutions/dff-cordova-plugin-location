package com.dff.cordova.plugin.location.actions;

import android.util.Log;

import com.dff.cordova.plugin.common.action.Action;
import com.dff.cordova.plugin.location.dagger.annotations.Shared;
import com.dff.cordova.plugin.location.resources.Res;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Action that deals with the location list in order to clear it.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 04.08.2017
 */
public class ClearLocationListAction extends Action {

    private static final String TAG = GetLocationListAction.class.getSimpleName();

    private Res mRes;

    @Inject
    public ClearLocationListAction(@Shared Res mRes) {
        this.mRes = mRes;
    }

    @Override
    public void execute() {
        JSONObject message = new JSONObject();
        int size = mRes.getLocationList().size();
        mRes.clearList();
        try {
            message.put("oldSize", size);
            message.put("currentSize", mRes.getLocationList().size());
        } catch (JSONException e) {
            Log.e(TAG, "Error: -->", e);
        }
        callbackContext.success(message);
    }
}
