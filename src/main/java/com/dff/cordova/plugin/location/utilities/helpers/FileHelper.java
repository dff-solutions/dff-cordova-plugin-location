package com.dff.cordova.plugin.location.utilities.helpers;

import android.content.Context;
import android.util.Log;
import com.dff.cordova.plugin.common.log.CordovaPluginLog;
import com.dff.cordova.plugin.location.resources.LocationResources;
import org.apache.cordova.LOG;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import dagger.Module;

/**
 * Class to read/write data in a file.
 *
 * @author Anthony Nahas
 * @version 4.0.0
 * @since 05.12.2016
 */
@Module
public class FileHelper {

    private static final String TAG = "FileHelper";

    /**
     * Store all locations that exists in the location list in a file.
     *
     * @param context context of the application/service
     */
    public static void storePendingLocation(Context context) {
        Log.d(TAG, "onStorePendingLocation()");
        PreferencesHelper preferencesHelper = new PreferencesHelper(context);
        FileOutputStream fos = null;
        ObjectOutputStream os;
        try {
            fos = context.openFileOutput(LocationResources.LOCATION_FILE_NAME, Context.MODE_PRIVATE);
            switch (preferencesHelper.getReturnType()) {
                case LocationResources.DFF_STRING:
                    ArrayList<String> pendingLocationDffString = LocationResources.getLocationListDffString();
                    if (pendingLocationDffString.size() > 0) {
                        Log.d(TAG, "PendingLocationsList count = " + pendingLocationDffString.size());
                        //Mode_Append / private
                        os = new ObjectOutputStream(fos);
                        for (String location : pendingLocationDffString) {
                            os.writeObject(location);
                        }
                        os.writeObject(null);
                        os.close();
                    }
                    break;
                case LocationResources.JSON:
                    ArrayList<JSONObject> pendingLocationJSON = LocationResources.getLocationListJson();
                    if (pendingLocationJSON.size() > 0) {
                        Log.d(TAG, "PendingLocationsList count = " + pendingLocationJSON.size());
                        os = new ObjectOutputStream(fos);
                        for (JSONObject location : pendingLocationJSON) {
                            os.writeObject(location.toString());
                        }
                        os.writeObject(null);
                        os.close();
                    }
                    break;
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
     *
     * @param context context of the application/service
     */
    public static void restorePendingLocation(Context context) {
        LOG.d(TAG, "onRestorePendingLocations()");
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        PreferencesHelper preferencesHelper = new PreferencesHelper(context);

        try {
            fis = context.openFileInput(LocationResources.LOCATION_FILE_NAME);
            int i = 0;
            if (fis.available() != 0) {
                Log.d(TAG, "fis is available on restoring pending locations!");
                ois = new ObjectInputStream(fis);

                String location;
                while ((location = (String) ois.readObject()) != null) {
                    switch (preferencesHelper.getReturnType()) {
                        case LocationResources.DFF_STRING:
                            if (LocationResources.getLocationListDffString() != null) {
                                LocationResources.addLocationToListAsDffString(location);
                                Log.d(TAG, "location " + i + " = " + location);
                                i++;
                            } else {
                                Log.d(TAG, "array (dff string) location list is null");
                            }
                            break;
                        case LocationResources.JSON:
                            if (LocationResources.getLocationListJson() != null) {
                                LocationResources.addLocationToListAsJson(new JSONObject(location));
                                Log.d(TAG, "location " + i + " = " + location);
                                i++;
                            } else {
                                Log.d(TAG, "array location list is null");
                            }
                            break;
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
                new PreferencesHelper(context).setLocationCanBeCleared(true);
            } catch (IOException e) {
                CordovaPluginLog.e(TAG, "Error: ", e);
            }
        }
    }


    public static void storeLocationsMultimap(Context context) {
        FileOutputStream fos = null;
        ObjectOutputStream os;

        try {
            Log.d(TAG, "on storeLocationsMultimap");
            fos = context.openFileOutput(LocationResources.LOCATIONS_MULTIMAP_FILE_NAME, Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);

            Map<String, Collection<JSONObject>> map = MultimapHelper.convertLocationsToJsonMultimap(LocationResources
                .getLocationsMultimap());

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

    public static void restoreLocationsMultimap(Context context) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = context.openFileInput(LocationResources.LOCATIONS_MULTIMAP_FILE_NAME);
            if (fis.available() != 0) {
                Log.d(TAG, "fis is available on restoring locations'multimap!");
                ois = new ObjectInputStream(fis);

                LocationResources
                    .setLocationsMultiMap(MultimapHelper
                        .convertMapToLocationsMultiMap(MultimapHelper
                            .parseJSONtoMap((String) ois.readObject())));

                Log.d(TAG, "on restoreLocationsMultimap");
                LocationResources.logLocationsMultimap();
            }
        } catch (IOException | ClassNotFoundException e) {
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