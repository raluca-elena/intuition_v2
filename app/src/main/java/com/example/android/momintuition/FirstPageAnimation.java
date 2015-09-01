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
import android.view.animation.Animation;

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
        CircularSeekBarAnimation anim = new CircularSeekBarAnimation(circularSeekbar, 0, 100);
        anim.setDuration(7000);
        circularSeekbar.startAnimation(anim);

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
