package com.dff.cordova.plugin.location.actions;

import android.util.Log;
import com.dff.cordova.plugin.common.action.Action;
import com.dff.cordova.plugin.location.dagger.annotations.Shared;
import com.dff.cordova.plugin.location.interfaces.IGLocation;
import com.dff.cordova.plugin.location.resources.Res;
import com.dff.cordova.plugin.location.resources.Resources;
import com.dff.cordova.plugin.location.utilities.helpers.LocationHelper;
import com.dff.cordova.plugin.location.utilities.helpers.TimeHelper;
import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Get the last good location from the service if it's available.
 * Good location means in this context: accuracy < 20m..
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 20.06.17
 */
public class GetLocationAction extends Action {

    private static final String TAG = GetLocationAction.class.getSimpleName();

    private Res mRes;
    private TimeHelper mTimeHelper;
    private LocationHelper mLocationHelper;

    @Inject
    public GetLocationAction
        (
            @Shared Res mRes,
            TimeHelper mTimeHelper,
            LocationHelper mLocationHelper
        ) {

        this.mRes = mRes;
        this.mTimeHelper = mTimeHelper;
        this.mLocationHelper = mLocationHelper;
    }

    @Override
    public void execute() {

        IGLocation location = mRes.getLocation();

        if (location != null) {
            if (!(mTimeHelper.getTimeAge(mRes.getLocation().getTime()) <= Resources.LOCATION_MAX_AGE)) {
                mRes.clearLocation();
                Log.d(TAG, "setLocation --> null");
                callbackContext.error("location is null --> deprecated");
                return;
            }
            JSONObject locationJSON = mLocationHelper.toJson(location);
            callbackContext.success(locationJSON);
            return;
        }
        callbackContext.error("last good location is null");
    }
}
