package com.dff.cordova.plugin.location;

import com.dff.cordova.plugin.location.actions.Action;
import com.dff.cordova.plugin.location.actions.index.IndexActions;
import com.dff.cordova.plugin.location.configurations.JSActions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
public class ActionsManager {

    private JSActions mJsActions;
    private IndexActions mIndexActions;
    private Map<String, Action> mActionsMap;

    @Inject
    public ActionsManager
        (
            JSActions mJsActions,
            IndexActions mIndexActions
        ) {
        this.mJsActions = mJsActions;
        this.mIndexActions = mIndexActions;

        init();
    }

    private void init() {
        mActionsMap = new HashMap<>();
        mActionsMap.put(mJsActions.clear_stopID, mIndexActions.mClearStopIDAction);
        mActionsMap.put(mJsActions.enable_locations_mapping, mIndexActions.mEnableMappingAction);
        mActionsMap.put(mJsActions.get_location, mIndexActions.mGetLocationAction);
        mActionsMap.put(mJsActions.get_location_list, mIndexActions.mGetLocationListAction);
        mActionsMap.put(mJsActions.get_total_distance, mIndexActions.mGetTotalDistanceAction);
        mActionsMap.put(mJsActions.restore_pending_locations, mIndexActions.mRestoreAction);
        mActionsMap.put(mJsActions.set_stopID, mIndexActions.mSetStopIdAction);
        mActionsMap.put(mJsActions.start_service, mIndexActions.mStartLocationServiceAction);
        mActionsMap.put(mJsActions.stop_service, mIndexActions.mStopLocationServiceAction);
    }

    public Action hash(String action) {
        return mActionsMap.get(action);
    }

    public ArrayList<String> allJSAction() {
        return mJsActions.all;
    }
}
