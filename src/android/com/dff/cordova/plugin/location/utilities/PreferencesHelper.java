package com.dff.cordova.plugin.location.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;

/**
 * Created by anahas on 06.12.2016.
 */
public class PreferencesHelper {

    private static final String TAG = "PreferenceHelper";
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public PreferencesHelper(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(LocationResources.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public boolean setCanLocationCanBeCleared(Boolean state) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(LocationResources.SP_KEY_CLEAR_LOCATIONS, state);
        //editor.putInt("counter", 12);
        Boolean res = editor.commit();
        Log.d(TAG, "Success of shared preference's commit is: " + res);
        return res;
    }

    public boolean getCanLocationBeCleared() {
        return mSharedPreferences.getBoolean(LocationResources.SP_KEY_CLEAR_LOCATIONS, false);
    }

}
