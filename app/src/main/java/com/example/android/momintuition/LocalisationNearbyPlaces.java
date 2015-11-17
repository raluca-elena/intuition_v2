package com.example.android.momintuition;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.LruCache;
import android.view.animation.Animation;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import org.json.JSONException;
import org.json.JSONObject;


public class LocalisationNearbyPlaces implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    Context context;
    //datapop should have the same size as the elements in the jsonResponse form server..no more entries
    public static String[][] dataPop = new String[100][4];
    public static LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(200);
    public static int len = -1;
    static String latLong;

    public LocalisationNearbyPlaces(final Context c, String lat, String lng){
        latLong = lat + "," + lng;
        context = c;
        final GoogleApiClient mGoogleApiClient = new GoogleApiClient
                .Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://52.11.50.74:9000";
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        sb.append("lat=" + lat);
        sb.append("&");
        sb.append("long=" + lng);
        url = sb.toString();
        //url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&key=AIzaSyBbE2wO2MDZ2goETgsY__ifEq2dlOMLLc4";
        JsonObjectRequest stringRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            FormatData d = new FormatData(response, dataPop);
                            len = d.len;
                            //DistanceMatrixTask dmatrix = new DistanceMatrixTask(context, dataPop);

                            for (int i = 0; i < len; i++) {
                                Log.i("--> id right now", dataPop[i][2] + "");
                                if (dataPop[i][2] != null) {
                                    new ImageTask(120, 120, mGoogleApiClient, dataPop[i][2]) {
                                        @Override
                                        protected void onPreExecute() {
                                        }

                                        @Override
                                        protected void onPostExecute(AttributedPhoto attributedPhoto) {

                                            if (attributedPhoto != null) {
                                                // Photo has been loaded, display it.
                                                Log.i("placeID - loaded photo", " " + this.placeID);
                                                mMemoryCache.put(this.placeID, attributedPhoto.bitmap);
                                                this.imageLoaded++;
                                                }
                                        }
                                    }.execute(dataPop[i][2]);


                                } else break;
                            }

                            Intent intent = new Intent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setClass(c, ActivityChooser.class);
                            c.startActivity(intent);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("this is the error", error + "");
            }
        });
        int MY_SOCKET_TIMEOUT_MS = 9000;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }




    @Override
    public void onConnected(Bundle bundle) {
        //Log.i("I am connected", "maps");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("conn suspended", " " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("onConnFailed", connectionResult + " ");

    }



}
