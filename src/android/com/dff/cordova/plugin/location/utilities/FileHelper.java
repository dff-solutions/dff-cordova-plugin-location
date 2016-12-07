package com.dff.cordova.plugin.location.utilities;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;
import org.apache.cordova.LOG;

import java.io.*;
import java.util.ArrayList;

/**
 * Class to read/write data in a file.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 05.12.2016
 */
public class FileHelper {

    private static final String TAG = "FileHelper";

    /**
     * Store all locations that exists in the location list in a file.
     *
     * @param context context of the application/service
     */
    public static void storePendingLocation(Context context) {
        Log.d(TAG, "onStorePendingLocation()");
        File file = null;
        FileOutputStream fos = null;
        ArrayList<String> pendingLocation = LocationResources.getLastGoodLocationList();


        try {
            File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
            boolean isPresent = true;
            if (!docsFolder.exists()) {
                Log.d(TAG, "Dir is not present");
                isPresent = docsFolder.mkdir();
            }
            if (isPresent) {
                Log.d(TAG, "Dir is present");
                file = new File(docsFolder.getAbsolutePath(), LocationResources.LOCATION_FILE_NAME);
            } else {
                // Failure
            }
            String path = file != null ? file.getName() : null;
            Log.d(TAG, "Path = " + path);
            //file = new File(LocationResources.LOCATION_EXTERNAL_FILE_DIRECTORY);
            //fos = context.openFileOutput(LocationResources.LOCATION_FILE_NAME, Context.MODE_APPEND); // Mode Private ?!
            File f = new File(LocationResources.LOCATION_FILE_NAME);
            fos = context.openFileOutput(LocationResources.LOCATION_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);

            Log.d(TAG, "PendingLocationsList count = " + pendingLocation.size());
            if (pendingLocation.size() > 0) {
                for (String location : pendingLocation) {
                    os.writeObject(location);
                }
                os.close();
            }

        } catch (IOException e) {
            Log.e(TAG, "Error: IOException", e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error: IOException", e);
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

        try {
            fis = context.openFileInput(LocationResources.LOCATION_FILE_NAME);
            int i = 0;
            if (fis.available() != 0) {
                Log.d(TAG, "fis is available!");
                ois = new ObjectInputStream(fis);

                while (true) {
                    Log.d(TAG, "string " + i + " = " + (String) ois.readObject());
                    i++;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                Log.d(TAG, "Error: ", e);
            }
        }
    }

}
