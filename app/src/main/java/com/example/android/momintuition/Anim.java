package com.example.android.momintuition;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;


public class Anim extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static CircularSeekBar circularSeekbar;
    FetchCoordinates coord;
    CircularSeekBarAnimation anim ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page_animation);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewGroup v = (ViewGroup) findViewById(R.id.layout);

        circularSeekbar = new CircularSeekBar(this);
        circularSeekbar.adjustRadius(3, 2);
        circularSeekbar.setMaxProgress(100);
        circularSeekbar.setProgress(0);
        circularSeekbar.setAngle(10);

        addContentView(circularSeekbar, v.getLayoutParams());
        anim = new CircularSeekBarAnimation(circularSeekbar, 0, 100);
        anim.setDuration(3000);
        anim.setInterpolator(new MVAccelerateDecelerateInterpolator());
        anim.setRepeatCount(2);
        circularSeekbar.startAnimation(anim);


        anim.setAnimationListener(new android.view.animation.Animation.AnimationListener() {


            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {

                coord = new FetchCoordinates(getApplicationContext());

            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                // TODO anim should end when activity ready => when all images loaded
                toolbar.setVisibility(View.GONE);

            }
        });

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

}
