package com.example.android.momintuition;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.util.LruCache;

import java.util.logging.Handler;

/*Provides corrent location and starts the nearbyPlacesReq*/

public class FetchCoordinates {

    public static double lati = 0.0;
    public static double longi = 0.0;

    public LocationManager locationManager;
    //public mLocationListener ll;

    Context c;

    FetchCoordinates(Context context) {
        c = context;
        locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);

        locationManager.requestSingleUpdate(criteria, new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                // reverse geo-code location
                double lati = location.getLatitude();
                double lon = location.getLongitude();
                Log.i("location", location.getLatitude() + " " + location.getLongitude());
                LocalisationNearbyPlaces local = new LocalisationNearbyPlaces(c, new Double(lati).toString(), new Double(lon).toString());

            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub

            }

        }, null);

    }

}


