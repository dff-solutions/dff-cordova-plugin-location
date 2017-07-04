package com.dff.cordova.plugin.location.configurations;

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
        mActionsMap.put(mJsActions.store_pending_locations, mIndexActions.mStoreAction);
        mActionsMap.put(mJsActions.restore_pending_locations, mIndexActions.mRestoreAction);
        mActionsMap.put(mJsActions.clear_stopID, mIndexActions.mClearStopIDAction);
        mActionsMap.put(mJsActions.enable_locations_mapping, mIndexActions.mEnableMappingAction);
        mActionsMap.put(mJsActions.get_location, mIndexActions.mGetLocationAction);
        mActionsMap.put(mJsActions.get_location_list, mIndexActions.mGetLocationListAction);
        mActionsMap.put(mJsActions.get_total_distance, mIndexActions.mGetTotalDistanceAction);
        mActionsMap.put(mJsActions.set_stopID, mIndexActions.mSetStopIdAction);
        mActionsMap.put(mJsActions.get_last_stopID, mIndexActions.mGetLastStopIDAction);
        mActionsMap.put(mJsActions.start_service, mIndexActions.mStartLocationServiceAction);
        mActionsMap.put(mJsActions.stop_service, mIndexActions.mStopLocationServiceAction);
        mActionsMap.put(mJsActions.get_multimap_keyset, mIndexActions.mGetKeySetFromLocationsMultimapAction);
        mActionsMap.put(mJsActions.register_location_listener, mIndexActions.mRegisterLocationListenerAction);
        mActionsMap.put(mJsActions.register_provider_listener, mIndexActions.mRegisterProviderListenerAction);
        mActionsMap.put(mJsActions.unregister_provider_listener, mIndexActions.mUnregisterProviderListenerAction);
        mActionsMap.put(mJsActions.register_stop_listener, mIndexActions.mRegisterStopListenerAction);
        mActionsMap.put(mJsActions.unregister_stop_listener, mIndexActions.mUnregisterStopListenerAction);
    }

    public Action hash(String action) {
        return mActionsMap.get(action);
    }

    public ArrayList<String> allJSAction() {
        return mJsActions.all;
    }
}
