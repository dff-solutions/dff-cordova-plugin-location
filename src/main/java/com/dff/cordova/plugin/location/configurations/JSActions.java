package com.dff.cordova.plugin.location.configurations;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * JsActions
 *
 * @author Anthony Nahas
 * @version 1.2
 * @since 23.06.17
 */
@Singleton
public class JSActions {

    private static final String TAG = JSActions.class.getSimpleName();

    //JS Actions
    //service
    public final String start_service = "location.action.START_SERVICE";
    public final String stop_service = "location.action.STOP_SERVICE";
    //location
    public final String get_location = "location.action.GET_LOCATION";
    public final String clear_location_list = "location.action.CLEAR_LOCATION_LIST";
    public final String get_location_list = "location.action.GET_LOCATION_LIST";
    //re-store
    public final String store_pending_locations = "location.action.intent.STORE_PENDING_LOCATIONS";
    public final String restore_pending_locations = "location.action.intent.RESTORE_PENDING_LOCATIONS";
    //distance calculation
    public final String get_multimap_keyset = "location.action.GET_KEY_SET_FROM_LOCATIONS_MULTI_MAP";
    public final String enable_locations_mapping = "location.action.ACTION_ENABLE_MAPPING_LOCATIONS";
    public final String set_stopID = "hash_map.action.SET_STOP_ID";
    public final String get_last_stopID = "hash_map.action.GET_LAST_STOP_ID";
    public final String clear_stopID = "hash_map.action.CLEAR_STOP_ID";
    public final String get_total_distance = "distance.action.GET_TOTAL_DISTANCE_CALCULATOR";
    //register
    public final String register_location_listener = "location.action.ACTION_REGISTER_LOCATION_LISTENER";
    public final String register_stop_listener = "location.action.ACTION_REGISTER_STOP_LISTENER";
    public final String register_provider_listener = "location.action.ACTION_REGISTER_PROVIDER_LISTENER";
    //unregister
    public final String unregister_location_listener = "location.action.ACTION_UNREGISTER_LOCATION_LISTENER";
    public final String unregister_stop_listener = "location.action.ACTION_UNREGISTER_STOP_LISTENER";
    public final String unregister_provider_listener = "location.action.ACTION_UNREGISTER_PROVIDER_LISTENER";

    @Inject
    public JSActions() {
    }

    public final ArrayList<String> all = getAll();

    private ArrayList<String> getAll() {
        ArrayList<String> actions = new ArrayList<>();

        for (Field field : getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                String action = (String) field.get(this);
                if (action != null) {
                    actions.add(action);
                }
            } catch (IllegalAccessException e) {
                Log.e(TAG, "Error while reflecting: ", e);
            }
        }
        return actions;
    }

}
