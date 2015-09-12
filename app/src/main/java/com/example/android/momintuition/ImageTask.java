package com.example.android.momintuition;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
/**
 * Created by rpodiuc on 9/9/15.
 */
abstract class ImageTask extends AsyncTask<String , Void, ImageTask.AttributedPhoto> {

    private int mHeight;
    private int mWidth;
    private String placeID;
    private GoogleApiClient mGoogleApiClient;

    public ImageTask(int width, int height, String PlaceID) {
        mHeight = height;
        mWidth = width;
        placeID = PlaceID;
        
    }

    public ImageTask(int width, int height, GoogleApiClient g) {
        mGoogleApiClient = g;
        mHeight = height;
        mWidth = width;
    }


    @Override
    protected AttributedPhoto doInBackground(String... params) {
        if (params.length != 1) {
            return null;
        }
        String placeId = params[0];
        if (placeId == null)
            placeId = "ChIJrTLr-GyuEmsRBfy61i59si0";
        AttributedPhoto attributedPhoto = null;


        PlacePhotoMetadataResult result = Places.GeoDataApi
                .getPlacePhotos(mGoogleApiClient, placeId).await();

        if (result.getStatus().isSuccess()) {
            PlacePhotoMetadataBuffer photoMetadata = result.getPhotoMetadata();
            if (photoMetadata.getCount() > 0 && !isCancelled()) {
                // Get the first bitmap and its attributions.
                PlacePhotoMetadata photo = photoMetadata.get(0);
                CharSequence attribution = photo.getAttributions();
                // Load a scaled bitmap for this photo.
                Bitmap image = photo.getScaledPhoto(mGoogleApiClient, 375, 299).await()
                        .getBitmap();

                attributedPhoto = new AttributedPhoto(attribution, image);
            }
            // Release the PlacePhotoMetadataBuffer.
            photoMetadata.release();
        }
        return attributedPhoto;
    }

    class AttributedPhoto {

        public final CharSequence attribution;
        public final Bitmap bitmap;

        public AttributedPhoto(CharSequence attribution, Bitmap bitmap) {
            this.attribution = attribution;
            this.bitmap = bitmap;
        }
    }

}



