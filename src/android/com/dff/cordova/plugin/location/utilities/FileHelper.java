package com.dff.cordova.plugin.location.utilities;

import android.content.Context;
import android.util.Log;
import com.dff.cordova.plugin.location.resources.LocationResources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by anahas on 05.12.2016.
 */
public class FileHelper {

    private static final String TAG = "FileHelper";

    public static void storePendingLocation(Context context) {
        File file = null;
        FileOutputStream fos = null;
        ArrayList<String> pendingLocation = LocationResources.getLastGoodLocationList();

        try {
            //file =
            fos = context.openFileOutput(LocationResources.LOCATION_FILE_NAME, Context.MODE_APPEND); // Mode Private ?!
            ObjectOutputStream os = new ObjectOutputStream(fos);

            if(pendingLocation.size() > 0){
                for(String location : pendingLocation){
                    os.writeObject(location);
                }
                os.close();
            }

        } catch (IOException e) {
            Log.e(TAG,"Error: IOException", e);
        }

        finally {
            try {
                fos.close();
            } catch (IOException e) {
                Log.e(TAG,"Error: IOException", e);
            }
        }
    }


}
