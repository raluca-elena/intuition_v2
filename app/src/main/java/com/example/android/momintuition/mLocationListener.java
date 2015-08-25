package com.example.android.momintuition;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

/**
 * Created by rpodiuc on 7/30/15.
 */

public class mLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {
        try {
            FetchCoordinates.lati = location.getLatitude();
            FetchCoordinates.longi = location.getLongitude();
            Log.i("LATI", FetchCoordinates.lati + "LONG " + FetchCoordinates.longi);

        } catch (Exception e) {
            Log.i("WHY THE HELL", "YOU DON't SAY ANYTHING!");
        }
        Looper.myLooper().quit();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("onStatusChanged***", status + "");
        Looper.myLooper().quit();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("onProviderEnabled", "onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("OnProviderDisabled", "OnProviderDisabled");
    }

}
