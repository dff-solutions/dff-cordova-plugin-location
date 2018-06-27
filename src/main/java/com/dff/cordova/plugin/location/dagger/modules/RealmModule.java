package com.dff.cordova.plugin.location.dagger.modules;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.dff.cordova.plugin.dagger2.annotations.ApplicationContext;
import com.dff.cordova.plugin.dagger2.annotations.Fallback;
import com.dff.cordova.plugin.dagger2.annotations.HasPermission;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by anahas on 22.01.2018.
 *
 * @author Anthony Nahas
 * @version 1.2
 * @since 22.01.18
 */
@Module
public class RealmModule {

    private static final String TAG = RealmModule.class.getSimpleName();

    private static final String REALM_NAME = "locations.realm";
    private static final int REALM_SCHEMA_VERSION = 0;


    @Provides
    public Realm provideRealm(@HasPermission RealmConfiguration realmConfiguration) {
        return Realm.getInstance(realmConfiguration);
    }

    @Provides
    @Fallback
    public RealmConfiguration provideRealmConfigurationAsFallback() {
        return new RealmConfiguration.Builder()
                .name(REALM_NAME)
                .schemaVersion(REALM_SCHEMA_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    @HasPermission
    @Provides
    public RealmConfiguration provideRealmConfiguration(@ApplicationContext Context context,
                                                        @Fallback RealmConfiguration realmFallbackConfiguration) {

        String externalStorageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String packageName = context.getApplicationContext().getPackageName();
        String path = externalStorageDir
                + File.separator
                + "Android"
                + File.separator
                + "data"
                + File.separator
                + packageName
                + File.separator
                + "realm"
                + File.separator;

        File file = new File(path);
        Log.d(TAG, "File: " + file.getAbsolutePath() + " exists: " + file.exists());
        Log.d(TAG, "File: " + file.getAbsolutePath() + " should be created: " + file.mkdirs());

        try {
            return new RealmConfiguration.Builder()
                    .directory(file)
                    .name(REALM_NAME)
                    .schemaVersion(REALM_SCHEMA_VERSION)
                    .deleteRealmIfMigrationNeeded()
                    .build();
        } catch (RuntimeException e) {
            Log.e(TAG, "Error: -->", e);
            return realmFallbackConfiguration;
        }
    }

}
