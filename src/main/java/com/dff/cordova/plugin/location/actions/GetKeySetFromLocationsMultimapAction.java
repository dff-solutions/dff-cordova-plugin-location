package com.dff.cordova.plugin.location.actions;

import com.dff.cordova.plugin.location.resources.Res;
import com.dff.cordova.plugin.location.resources.Resources;

import org.json.JSONArray;

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
public class GetKeySetFromLocationsMultimapAction extends Action {

    private Res mRes;

    @Inject
    public GetKeySetFromLocationsMultimapAction(Res mRes) {
        this.mRes = mRes;
    }

    @Override
    public void execute() {
        JSONArray jsonArray = new JSONArray(new ArrayList<>(mRes.getLocationListMultimap().keySet()));
        callbackContext.success(jsonArray);
    }
}
