package com.dff.cordova.plugin.location.actions;

import android.location.Location;
import android.util.Log;

import com.dff.cordova.plugin.common.action.Action;
import com.dff.cordova.plugin.location.dagger.annotations.Shared;
import com.dff.cordova.plugin.location.resources.Res;
import com.dff.cordova.plugin.location.resources.Resources;
import com.dff.cordova.plugin.location.simulators.DistanceSimulator;

import org.apache.cordova.CallbackContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by anahas on 23.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 23.06.17
 */
@Singleton
public class ClearStopIDAction extends Action {

    private static final String TAG = ClearStopIDAction.class.getSimpleName();

    private Res mRes;
    private DistanceSimulator mDistanceSimulator;

    @Inject
    public ClearStopIDAction(@Shared Res mRes, DistanceSimulator mDistanceSimulator) {
        this.mRes = mRes;
        this.mDistanceSimulator = mDistanceSimulator;
    }

    @Override
    public void execute() {
        try {
            JSONObject params = args.getJSONObject(0);
            String requestedStopID = params.optString(Resources.JSON_KEY_STOP_ID, Resources.STOP_ID);
            if (params.optBoolean(Resources.RESET, false)) {
                Resources.STOP_ID = "UNKNOWN";
            }
            ArrayList<Location> locationsList = new ArrayList<>(mRes.getLocationListMultimap().get(requestedStopID));
            if (locationsList.isEmpty()) {
                callbackContext.error
                    (TAG
                        + " : "
                        + "Error -->  arraylist of stopid isEmpty - size = 0"
                        + "\t requested stop id --> "
                        + requestedStopID);
                return;
            }
            mDistanceSimulator.performDistanceCalculation(callbackContext, requestedStopID, locationsList);
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
    }
}
