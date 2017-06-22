package com.dff.cordova.plugin.location.actions;

import android.util.Log;

import com.dff.cordova.plugin.location.abstracts.Action;
import com.dff.cordova.plugin.location.resources.LocationResources;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by anahas on 22.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 22.06.17
 */
@Singleton
public class SetStopIdAction extends Action {

    private static final String TAG = SetStopIdAction.class.getSimpleName();

    @Inject
    public SetStopIdAction() {
    }

    @Override
    public void execute() {
        try {
            JSONObject params = super.getArguments().getJSONObject(0);
            LocationResources.STOP_ID = params.optString(LocationResources.JSON_KEY_STOP_ID, LocationResources.STOP_ID);
            super.getCallbackContext().success();
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
            super.getCallbackContext().error("Error: " + e);
        }
    }
}
