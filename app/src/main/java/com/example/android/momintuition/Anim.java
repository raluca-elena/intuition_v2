package com.example.android.momintuition;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import org.json.JSONException;
import org.json.JSONObject;


public class Anim extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    CircularSeekBar circularSeekbar;
    public static String[][] dataPop = new String[100][4];
    public static String[][] places = new String[100][2];

    public static LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(20);



    public  String getThemeName()
    {
        PackageInfo packageInfo;
        try
        {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            int themeResId = packageInfo.applicationInfo.theme;
            return getResources().getResourceEntryName(themeResId);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return null;
        }
    }
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Log.i("BLA", getThemeName() + "");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page_animation);
        final ActionBar actionBar = getSupportActionBar();
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#40000000")));


        ViewGroup v = (ViewGroup) findViewById(R.id.abc);

        circularSeekbar = new CircularSeekBar(this);
        circularSeekbar.adjustRadius(6, 2);
        circularSeekbar.setMaxProgress(100);
        circularSeekbar.setProgress(0);
        circularSeekbar.setAngle(10);

        addContentView(circularSeekbar, v.getLayoutParams());
        final CircularSeekBarAnimation anim = new CircularSeekBarAnimation(circularSeekbar, 0, 100);
        anim.setDuration(3000);
        anim.setInterpolator(new MVAccelerateDecelerateInterpolator());
        anim.setRepeatCount(2);
        circularSeekbar.startAnimation(anim);


        anim.setAnimationListener(new android.view.animation.Animation.AnimationListener() {


            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                // TODO Start your activity here.
                Intent intent = new Intent(getApplicationContext(), ActivityChooser.class);
                startActivity(intent);
                actionBar.hide();

                //startActivity(aboutIntent); // Here you go.

            }
        });

        final GoogleApiClient mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://52.11.50.74:9000";
        //url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&key=AIzaSyBbE2wO2MDZ2goETgsY__ifEq2dlOMLLc4";
        JsonObjectRequest stringRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //anim.cancel();
                        // Display the first 500 characters of the response string.
                        Log.i("THIS IS ANIM", response + "");
                        try {
                            FormatData d = new FormatData(response, dataPop);
                            DistanceMatrixTask dmatrix = new DistanceMatrixTask(getApplicationContext(),dataPop);

                            for (int i = 0; i < dataPop.length; i++){
                                Log.i("id right now", dataPop[i][2] + "" );
                                if (dataPop[i][2] != null){
                                new ImageTask(120, 120,  mGoogleApiClient, dataPop[i][2]) {
                                    @Override
                                    protected void onPreExecute() {
                                        // Display a temporary image to show while bitmap is loading.

                                        //h.img.setImageResource(R.drawable.bear);
                                        Log.i("on pre exec--", this.placeID + "");
                                    }

                                    @Override
                                    protected void onPostExecute(AttributedPhoto attributedPhoto) {

                                        if (attributedPhoto != null) {
                                            // Photo has been loaded, display it.
                                            mMemoryCache.put(this.placeID, attributedPhoto.bitmap);
                                            this.imageLoaded++;
                                            Log.i("image loaded", this.imageLoaded + "");
                                        }
                                    }
                                }.execute(dataPop[i][2]);


                                } else break;}



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
        int  MY_SOCKET_TIMEOUT_MS = 9000;
        Log.i("max retries", DefaultRetryPolicy.DEFAULT_MAX_RETRIES + "");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("I am connected", "maps");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("conn suspended", " " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("onConnFailed", connectionResult + " ");

    }


    public class MVAccelerateDecelerateInterpolator implements Interpolator {

        // easeInOutQuint
        public float getInterpolation(float t) {
            float x = t*2.0f;
            if (t<0.5f) return 0.5f*x*x*x*x*x;
            x = (t-0.5f)*2-1;
            return 0.5f*x*x*x*x*x+1;
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_page_animation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.i("ZOMG", "Asdf");

            //anim.startNow();

            //View parent = (View)circularSeekbar.getParent();

            //parent.invalidate();
            //circularSeekbar.requestLayout();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
