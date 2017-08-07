package com.dff.cordova.plugin.location.utilities.helpers;

import android.content.Context;
import android.util.Log;

import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.location.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.location.dagger.annotations.Shared;
import com.dff.cordova.plugin.location.resources.Res;
import com.dff.cordova.plugin.location.resources.Resources;

import org.apache.cordova.LOG;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class to read/write data in a file.
 *
 * @author Anthony Nahas
 * @version 4.0.0
 * @since 05.12.2016
 */
@Singleton
public class FileHelper {

    private static final String TAG = "FileHelper";

    private Context mContext;
    private PreferencesHelper mPreferencesHelper;
    private MultimapHelper mMultimapHelper;
    private Res mRes;

    @Inject
    public FileHelper(
        @ApplicationContext Context mContext,
        @Shared Res mRes,
        PreferencesHelper mPreferencesHelper,
        MultimapHelper mMultimapHelper) {

        this.mContext = mContext;
        this.mRes = mRes;
        this.mPreferencesHelper = mPreferencesHelper;
        this.mMultimapHelper = mMultimapHelper;
    }

    /**
     * Store all locations that exists in the location list in a file.
     */
    public void storePendingLocation() {
        Log.d(TAG, "onStorePendingLocation()");
        FileOutputStream fos = null;
        ObjectOutputStream os;
        try {
            fos = mContext.openFileOutput(Resources.LOCATION_FILE_NAME, Context.MODE_PRIVATE);

            List<JSONObject> pendingLocationJSON = mRes.getLocationList();
            if (pendingLocationJSON.size() > 0) {
                Log.d(TAG, "PendingLocationsList count = " + pendingLocationJSON.size());
                os = new ObjectOutputStream(fos);
                for (JSONObject location : pendingLocationJSON) {
                    os.writeObject(location.toString());
                }
                os.writeObject(null);
                os.close();
            }

        } catch (IOException e) {
            CordovaPluginLog.e(TAG, "Error: ", e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                CordovaPluginLog.e(TAG, "Error: ", e);
            }
        }
    }

    /**
     * Restore all locations from file to the locations list.
     */
    public void restorePendingLocation() {
        LOG.d(TAG, "onRestorePendingLocations()");
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = mContext.openFileInput(Resources.LOCATION_FILE_NAME);
            int i = 0;
            if (fis.available() != 0) {
                Log.d(TAG, "fis is available on restoring pending locations!");
                ois = new ObjectInputStream(fis);

                String location;
                while ((location = (String) ois.readObject()) != null) {
                    if (mRes.getLocationList() != null) {
                        mRes.addLocation(new JSONObject(location));
                        Log.d(TAG, "location " + i + " = " + location);
                        i++;
                    } else {
                        Log.d(TAG, "array location list is null");
                    }
                }
            }

        } catch (IOException | ClassNotFoundException | JSONException e) { //
            CordovaPluginLog.e(TAG, "Error: ", e);
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
                mPreferencesHelper.setLocationCanBeCleared(true);
            } catch (IOException e) {
                CordovaPluginLog.e(TAG, "Error: ", e);
            }
        }
    }


    public void storeLocationsMultimap() {
        FileOutputStream fos = null;
        ObjectOutputStream os;

        try {
            Log.d(TAG, "on storeLocationsMultimap");
            fos = mContext.openFileOutput(Resources.LOCATIONS_MULTIMAP_FILE_NAME, Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);

            Map<String, Collection<JSONObject>> map = mMultimapHelper.convertLocationsToJsonMultimap
                (mRes.getLocationListMultimap());

            JSONObject jsonObject = new JSONObject(map);
            Log.d(TAG, jsonObject.toString());

            os.writeObject(jsonObject.toString());
            os.writeObject(null);
            os.close();
        } catch (IOException e) {
            CordovaPluginLog.e(TAG, "Error: ", e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                CordovaPluginLog.e(TAG, "Error: ", e);
            }
        }
    }

    public void restoreLocationsMultimap() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = mContext.openFileInput(Resources.LOCATIONS_MULTIMAP_FILE_NAME);
            if (fis.available() != 0) {
                Log.d(TAG, "fis is available on restoring locations'multimap!");
                ois = new ObjectInputStream(fis);

//                Resources
//                    .setLocationsMultiMap(MultimapHelper
//                        .convertMapToLocationsMultiMap(MultimapHelper
//                            .parseJSONtoMap((String) ois.readObject())));

                Log.d(TAG, "on restoreLocationsMultimap");
                mRes.logLocationListMultimap();
            }
        } catch (IOException e) {
            CordovaPluginLog.e(TAG, "Error: ", e);
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                CordovaPluginLog.e(TAG, "Error: ", e);
            }
        }
    }
}
