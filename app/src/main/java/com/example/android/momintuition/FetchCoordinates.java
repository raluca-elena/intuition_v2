package com.example.android.momintuition;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

public class FetchCoordinates extends AsyncTask<String, Void, String> {

    public static double lati = 0.0;
    public static double longi = 0.0;

    public LocationManager mLocationManager;
    public mLocationListener ll;

    Context c;

    FetchCoordinates(Context context) {
        c = context;
    }

    @Override
    protected void onPreExecute() {
        Log.i("onPreExecute FecthCoord", "doing nothing hire");

    }

    @Override
    protected void onCancelled() {
        mLocationManager.removeUpdates(ll);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("ON POSTEXEC", "FetchCoord");
        String[] params = new String[3];

        // do a call
        params[0] = "52.11.50.74:9000/";
        Intent i = new Intent(c, ActivityChooser.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        c.startActivity(i);
        //Looper.myLooper().quit();

        //params[1] = user;
        //params[2] = password;

        //this should happen after getting the coord if you do if not a default page should be served
        //AsyncTask<String, Void, String> fm = new RequestTask().execute(params);

        //RecyclerView rv = new RecyclerView(c);
        //final LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //rv.setLayoutManager(layoutManager);

    }


    //this will return all the top locations that's whi we have a String result
    @Override
    protected String doInBackground(String... params) {

        Log.i("LOcation update", "YEY");
        ll = new mLocationListener();
        //????
        mLocationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        Looper.prepare();

        mLocationManager.requestSingleUpdate(mLocationManager.GPS_PROVIDER, ll, null);
        Looper.loop();
        return "DONE";
    }


}


