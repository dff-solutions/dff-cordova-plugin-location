package com.dff.cordova.plugin.location.actions;

import android.util.Log;
import com.dff.cordova.plugin.common.action.Action;
import com.dff.cordova.plugin.location.resources.Resources;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Created by anahas on 22.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 22.06.17
 */
public class SetStopIdAction extends Action {

    private static final String TAG = SetStopIdAction.class.getSimpleName();

    @Inject
    public SetStopIdAction() {
    }

    @Override
    public void execute() {
        try {
            JSONObject params = args.getJSONObject(0);
            Resources.STOP_ID = params.optString(Resources.JSON_KEY_STOP_ID, Resources.STOP_ID);
            callbackContext.success();
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
            callbackContext.error("Error: " + e);
        }
    }
}
