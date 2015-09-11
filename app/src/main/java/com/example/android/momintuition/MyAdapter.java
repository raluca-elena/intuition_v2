package com.example.android.momintuition;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    GoogleApiClient mGoogleApiClient;
    final View.OnClickListener optionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("this view was clicked", "GET THE ID ");
        }
    };
    private String[][] mDataset = new String [4][2];


    public MyAdapter(String[][] myDataset, GoogleApiClient mGoogleApiClient) {
        mDataset = myDataset;
        this.mGoogleApiClient = mGoogleApiClient;
        Log.i("DataSet Node Server" , myDataset.length + "");
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.title.setText(mDataset[position][0]);
        holder.activity.setText(mDataset[position][1]);

        Log.i("M_DATA   ",  mDataset[position][2] + "");
        placePhotosTask(holder,mDataset[position][2]);


        if (position % 2 == 0) {
            holder.img.setCropToPadding(true);
            holder.img.setImageResource(R.drawable.bear);

        }

        holder.itemView.setOnClickListener(optionListener);

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
            activity = (TextView) v.findViewById(R.id.activity);
            img = (ImageView) v.findViewById(R.id.image);

        }
    }

    private void placePhotosTask(ViewHolder holder, String placeID){
        final String placeId = placeID;
        final ViewHolder h = holder;
        // Create a new AsyncTask that displays the bitmap and attribution once loaded.
        Log.i("h.img.getWidth()", h.img.getWidth()+ "");
        Log.i( "h.img.getHeight()",  h.img.getHeight() + "");


        new ImageTask(h.img.getWidth(), h.img.getHeight(),  mGoogleApiClient) {
            @Override
            protected void onPreExecute() {
                // Display a temporary image to show while bitmap is loading.
                h.img.setImageResource(R.drawable.bear);
            }

            @Override
            protected void onPostExecute(AttributedPhoto attributedPhoto) {
                if (attributedPhoto != null) {
                    // Photo has been loaded, display it.
                    h.img.setImageBitmap(attributedPhoto.bitmap);

                }
            }
        }.execute(placeId);
    }

}