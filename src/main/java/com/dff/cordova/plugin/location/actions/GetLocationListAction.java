package com.dff.cordova.plugin.location.actions;

import android.util.Log;
import com.dff.cordova.plugin.dagger2.abstracts.Action;
import com.dff.cordova.plugin.dagger2.annotations.Shared;
import com.dff.cordova.plugin.location.interfaces.IGLocation;
import com.dff.cordova.plugin.location.resources.Res;
import com.dff.cordova.plugin.location.resources.Resources;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.List;

/**
 * Get the all pending location that are stored in the location list.
 * These location has been stored to be sent back later.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 21.06.2017
 */
public class GetLocationListAction extends Action {

    private static final String TAG = GetLocationListAction.class.getSimpleName();

    private Res mRes;

    @Inject
    public GetLocationListAction(@Shared Res mRes) {
        this.mRes = mRes;
    }


    @Override
    public void execute() {
        Boolean canReset = true;
        try {
            JSONObject params = getArgs().getJSONObject(0);
            if (params != null) {
                canReset = params.optBoolean(Resources.RESET, true);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
        }
        if (mRes.getLocationList().size() > 0) {
            List<IGLocation> clonedList = mRes.getLocationList();
            Gson gson = new Gson();
            try {
                if (clonedList != null) {
                    getCallbackContext().success(new JSONArray(gson.toJson(clonedList)));
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error: ", e);
                getCallbackContext().error("JSONException + " + e);
            }

            Log.d(TAG, "list > 0 ");
            if (canReset) {
                mRes.clearList();
            }
        } else {
            getCallbackContext().success(new JSONArray());
            Log.d(TAG, "list < 0 ");
        }
    }
}
