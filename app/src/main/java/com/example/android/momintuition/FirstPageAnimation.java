package com.example.android.momintuition;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONException;
import org.json.JSONObject;


public class FirstPageAnimation extends ActionBarActivity {
    CircularSeekBar circularSeekbar;
    static String[][] dataPop = new String[100][3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page_animation);
        ViewGroup v = (ViewGroup) findViewById(R.id.abc);

        circularSeekbar = new CircularSeekBar(this);
        circularSeekbar.adjustRadius(6, 2);
        circularSeekbar.setMaxProgress(100);
        circularSeekbar.setProgress(0);

        circularSeekbar.setAngle(10);

      /*  circularSeekbar.setSeekBarChangeListener(new CircularSeekBar.OnSeekChangeListener() {

            @Override
            public void onProgressChange(CircularSeekBar view, int newProgress) {

                Log.i("progress so far", view.getProgress() + "");

                //Log.i("progress", newProgress + "");
                //circularSeekbar.setProgress(newProgress );

            }
        });*/

        addContentView(circularSeekbar, v.getLayoutParams());
        final CircularSeekBarAnimation anim = new CircularSeekBarAnimation(circularSeekbar, 0, 100);
        anim.setDuration(7000);
        anim.setInterpolator(new MVAccelerateDecelerateInterpolator());
        anim.setRepeatCount(3);
        circularSeekbar.startAnimation(anim);
        //danger
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://52.11.50.74:9000";
        //url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&key=AIzaSyBbE2wO2MDZ2goETgsY__ifEq2dlOMLLc4";
        JsonObjectRequest stringRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //anim.cancel();
                        // Display the first 500 characters of the response string.
                        Log.i("THIS IS THE RESPONSE", response + "");
                        try {
                            FormatData d = new FormatData(response, dataPop);
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
        int  MY_SOCKET_TIMEOUT_MS = 1000;
        Log.i("max retries", DefaultRetryPolicy.DEFAULT_MAX_RETRIES + "");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Add the request to the RequestQueue.
        queue.add(stringRequest);




        //danger




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
