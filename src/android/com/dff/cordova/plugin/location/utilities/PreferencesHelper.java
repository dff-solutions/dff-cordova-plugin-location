package com.dff.cordova.plugin.location.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;

/**
 * Class to save and restore shared preferences.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 06.12.2016
 */
public class PreferencesHelper {

    private static final String TAG = "PreferenceHelper";
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public PreferencesHelper(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(LocationResources.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Store a key/value in the shared preferences in order to make a state always available.
     *
     * @param state the state to be stored true/false
     * @return whether the state has been successfully stored
     */
    public boolean setLocationCanBeCleared(Boolean state) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(LocationResources.SP_KEY_CLEAR_LOCATIONS, state);
        //editor.putInt("counter", 12);
        Boolean res = editor.commit();
        Log.d(TAG, "Success of shared preference's commit is: " + res);
        return res;
    }

    /**
     * Restore the state of "can Location be cleared"
     *
     * @return The state
     */
    public boolean getCanLocationBeCleared() {
        return mSharedPreferences.getBoolean(LocationResources.SP_KEY_CLEAR_LOCATIONS, false);
    }

}
