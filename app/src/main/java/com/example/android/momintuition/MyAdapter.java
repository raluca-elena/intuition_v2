package com.example.android.momintuition;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    GoogleApiClient mGoogleApiClient;
    LruCache<String, Bitmap> bitmapLruCache;
    private String[][] mDataset;
    String latLong;
    Context c;

    public class MyOnClickListener implements View.OnClickListener {

        String latlng;
        String source;
        String type;

        public MyOnClickListener(String latlng, String type) {
            this.latlng = latlng;
            this.type = type;
            source = latLong;

            //Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
            //        Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
            //c.startActivity(intent);


        }

        @Override
        public void onClick(View v) {
            //read your lovely variable
            Log.i("lat", latlng);

            Intent intent = new Intent(c, DirectionsActivity.class);
            intent.putExtra("START", source);
            intent.putExtra("DESTINATION", latlng);
            //c.startActivity(i);
            // get teh parsed locations coord

            // Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
            //         Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
            c.startActivity(intent);
        }

    }

    ;


    ////

    public class OnClickListenerDirections implements View.OnClickListener {

        String latlng;
        String source;
        String type;

        public OnClickListenerDirections(String latlng, String type) {
            this.latlng = latlng;
            this.type = type;
            source = latLong;

            //Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
            //        Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
            //c.startActivity(intent);


        }

        @Override
        public void onClick(View v) {
            //read your lovely variable
            Log.i("lat", latlng);

            //Intent intent = new Intent(c, DirectionsActivity.class);
            //intent.putExtra("START", source);
            //intent.putExtra("DESTINATION", latlng);
            ///


            Log.i("source ->> ", source);
            String url = "http://maps.google.com/maps?saddr" + source +  "&daddr=" + latlng;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    //Uri.parse("http://maps.google.com/maps?saddr=45.7062727,27.1826593&daddr=45.7066196,27.1826913"));
                    Uri.parse(url));





            //c.startActivity(i);
            // get teh parsed locations coord

            // Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
            //         Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));


            c.startActivity(intent);
        }}






    ////
    final View.OnClickListener optionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("this view was clicked", "GET THE ID " + v.toString());
            //getWindow().getDecorView().invalidate();

            Intent intent = new Intent(c, DirectionsActivity.class);
            //c.startActivity(i);
            // get teh parsed locations coord

           // Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
           //         Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
            c.startActivity(intent);
        }
    };



    public MyAdapter(Context c, String[][] myDataset, GoogleApiClient mGoogleApiClient,  LruCache<String, Bitmap> bitmapLruCache, String latLong) {
        this.c = c;
        mDataset = myDataset;
        this.mGoogleApiClient = mGoogleApiClient;
        this.bitmapLruCache = bitmapLruCache;
        this.latLong = latLong;
        Log.i("DataSet Node Server" , myDataset.length + "");
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.title.setText(mDataset[position][0]);


//        holder.activity.setText(mDataset[position][1]);

        if (mDataset[position][2] == null){
            Log.i("get no entry rolling", "yellowbear in bind");
            holder.img.setImageResource(R.drawable.bear);}
        else if (bitmapLruCache.get(mDataset[position][2]) == null) {
            holder.img.setImageResource(R.drawable.bear);
            placePhotosTask(holder, mDataset[position][2]);
            holder.img.setCropToPadding(true);
        } else {
            Log.i("GOT IMG in CACHE", mDataset[position][2]);
            holder.img.setImageBitmap(bitmapLruCache.get(mDataset[position][2]));
        }
        //holder.itemView.setOnClickListener(optionListener);
        holder.itemView.findViewById(R.id.map).setOnClickListener(new MyOnClickListener(mDataset[position][3], "car"));
        holder.itemView.findViewById(R.id.directions).setOnClickListener(new OnClickListenerDirections(mDataset[position][3], "car"));

    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {


        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spot, parent, false);

        return new MyAdapter.ViewHolder(v);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView activity;
        public ImageView img;

        public ViewHolder(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.title);
            //activity = (TextView) v.findViewById(R.id.activity);
            img = (ImageView) v.findViewById(R.id.image);

        }
    }

    private void placePhotosTask(ViewHolder holder, final String placeID){
        final String placeId = placeID;
        final ViewHolder h = holder;
        // Create a new AsyncTask that displays the bitmap and attribution once loaded.
        Log.i("NOT IN CACHE ", placeID);


        new ImageTask(h.img.getWidth(), h.img.getHeight(),  mGoogleApiClient) {
            @Override
            protected void onPreExecute() {
                // Display a temporary image to show while bitmap is loading.

                //h.img.setImageResource(R.drawable.bear);
            }

            @Override
            protected void onPostExecute(AttributedPhoto attributedPhoto) {

                if (attributedPhoto != null && attributedPhoto.bitmap != null) {
                    // Photo has been loaded, display it.
                    Log.i("HALABALULA", "ALOHA");
                    h.img.setImageBitmap(attributedPhoto.bitmap);
                    if (placeID != null){
                        bitmapLruCache.put(placeID, attributedPhoto.bitmap);
                        this.imageLoaded++;
                    }
                }
            }
        }.execute(placeId);
    }

}