package com.dff.cordova.plugin.location.classes;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import com.dff.cordova.plugin.location.LocationPluginTest;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.annotation.Config;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import static junit.framework.Assert.*;

@Config(manifest = Config.NONE)
public class GLocationTest extends LocationPluginTest {

    private static final String TAG = GLocationTest.class.getSimpleName();

    private GLocation mRandomLocation;

    private Generator mGenerator;

    public GLocationTest() {
        mGenerator = new Generator();
    }

    @Before
    public void setUp() {
        mRandomLocation = mGenerator.randomizeLocation();
        Log.d(TAG, "on setup --> new random location " + mRandomLocation);
    }

    @Test
    public void testHashForEmptyGLocationObj() throws Exception {
        GLocation gLocation = new GLocation();
        assertTrue(gLocation.hashCode() != 0);
    }

    @Test
    public void testToJsonMethod() throws Exception {
        assertNotNull(mRandomLocation);
    }

    @Test
    public void testFromJsonMethod() throws Exception {
        JSONObject jsonLocation = new JSONObject();
        jsonLocation.put(GLocation.LNG, 49.212525);
        jsonLocation.put(GLocation.LAT, 1.2865);
        jsonLocation.put(GLocation.ACC, 20.0);
        jsonLocation.put(GLocation.SPD, 12.5);
        jsonLocation.put(GLocation.BEARING, 280);
        jsonLocation.put(GLocation.TIME, System.currentTimeMillis());

        assertNotNull(new GLocation().fromJson(jsonLocation.toString()));
    }

    @Test
    public void testFromJsonMethodAsString() throws Exception {
        JSONObject jsonLocation = new JSONObject();
        jsonLocation.put(GLocation.LNG, "50.215125");
        jsonLocation.put(GLocation.LAT, "9.215125");
        jsonLocation.put(GLocation.ACC, "20");
        jsonLocation.put(GLocation.SPD, "50");
        jsonLocation.put(GLocation.BEARING, "300");
        jsonLocation.put(GLocation.TIME, String.valueOf(System.currentTimeMillis()));

        String stringLocation = jsonLocation.toString();

        assertNotNull(new GLocation().fromJson(stringLocation));
    }

    @Test
    public void testFromJSONToBeNull() throws Exception {
        JSONObject jsonLocation = new JSONObject();
        jsonLocation.put(GLocation.LNG, 49.212525);
        jsonLocation.put(GLocation.LAT, 1.2865);
        jsonLocation.put(GLocation.ACC, 20.0);
        jsonLocation.put(GLocation.SPD, 12.5);
        jsonLocation.put(GLocation.BEARING, 280);
        jsonLocation.put(GLocation.TIME, new Date().toString());

        assertNull("new location should be null --> should fail to convert json because of the date string ( " +
            "parse exc" +
            ")", new GLocation().fromJson(jsonLocation.toString()));

        assertNull("parsing to Glocation class should be fail when the string input is not valid",
            new GLocation().fromJson("should fail, because this param is invalid"));
    }

    @After
    public void tearDown() throws Exception {

    }

    private class Generator {

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private double randomizeDouble(int max, int min) {
            return ThreadLocalRandom.current().nextDouble(min, max + 1);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private float randomizeFloat() {
            return (float) ThreadLocalRandom.current().nextFloat();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private float randomizeFloat(int max, int min) {
            return (float) ThreadLocalRandom.current().nextDouble(min, max + 1);
        }

        public GLocation randomizeLocation() {
            GLocation randomLocation = new GLocation();
            randomLocation.setLongitude(this.randomizeFloat(100, 10));
            randomLocation.setLatitude(this.randomizeFloat(50, 0));
            randomLocation.setAltitude(this.randomizeFloat(20, 0));
            randomLocation.setSpeed(this.randomizeFloat());
            randomLocation.setBearing(this.randomizeFloat(360, 0));
            randomLocation.setTime(System.currentTimeMillis());

            return randomLocation;
        }
    }
}
