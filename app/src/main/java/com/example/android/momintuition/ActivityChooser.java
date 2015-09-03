package com.example.android.momintuition;

import android.app.Activity;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class ActivityChooser extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;//1 min/2
    static boolean canGetLocation = false;
    Location location;
    double latitude;
    double longitude;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    static String[][] dataPop = new String[100][2];
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_chooser);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // data set should come from AWS. next step to implement
        //String[] myDataset = new String[4];
        //myDataset[0] = "play";
        //myDataset[1] = "eat";



        //DANGER
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://52.11.50.74:9000";
        //url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&key=AIzaSyBbE2wO2MDZ2goETgsY__ifEq2dlOMLLc4";
        JsonObjectRequest stringRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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



        //DANGER


        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        AsyncTask as = new AsyncTask() {
            @Override
            protected Void doInBackground(Object... params) {

                PlacePhotoMetadataResult result = Places.GeoDataApi
                        .getPlacePhotos(mGoogleApiClient, "ChIJN1t_tDeuEmsRUsoyG83frY4").await();
// Get a PhotoMetadataBuffer instance containing a list of photos (PhotoMetadata).
                Log.i("the status is ", result.getStatus() + "");
                //new BufferedWriter(new FileWriter(getExternalFilesDir() + "zuzu"));

                if (result != null && result.getStatus().isSuccess()) {
                    Log.i("I've got  photo", "photo");
                    PlacePhotoMetadataBuffer photoMetadataBuffer = result.getPhotoMetadata();
                    if (photoMetadataBuffer.getCount() > 0 && !isCancelled()) {
                        // Get the first bitmap and its attributions.
                        PlacePhotoMetadata photo = photoMetadataBuffer.get(0);
                        CharSequence attribution = photo.getAttributions();
                        // Load a scaled bitmap for this photo.
                        Bitmap image = photo.getPhoto(mGoogleApiClient).await()
                                .getBitmap();





                        //danger
                        if  (image != null)
                            Log.i("GOT a true ", "BITMAP");
                        String root = Environment.getExternalStorageDirectory().toString();
                        File placesDir = new File(root + "/placessx");
                        placesDir.mkdirs();
                        Random generator = new Random();
                        int n = 10000;
                        n = generator.nextInt(n);
                        String fname = "Image-" + n + ".jpg";
                        File file = new File(placesDir, fname);
                        Log.i("", "" + file);
                       // if (file.exists())
                       //     file.delete();
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            image.compress(Bitmap.CompressFormat.JPEG, 90, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        //danger



                    }
                    // Release the PlacePhotoMetadataBuffer.
                    photoMetadataBuffer.release();
                }

                Log.i("this async is", "UNSUCCESS");

                return null;}
        };
        as.execute();

        mAdapter = new MyAdapter(dataPop, mGoogleApiClient);
        mRecyclerView.setAdapter(mAdapter);





//TO INVESTIGATE
        FetchCoordinates fetchCordinates = new FetchCoordinates(getApplicationContext());
        fetchCordinates.execute();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_chooser, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("ON Connected", "photo");




    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

/////

}


/////





