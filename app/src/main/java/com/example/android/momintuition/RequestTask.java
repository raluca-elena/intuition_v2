package com.example.android.momintuition;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by rpodiuc on 7/16/15.
 */


class RequestTask extends AsyncTask<String, Void, String> {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... uri) {
        Log.i("this is my castle", "BURP");
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String response = null;


        Uri.Builder ub = new Uri.Builder();
        ub.scheme("http");
        ub.encodedAuthority(uri[0]);
        URL url = null;
        try {
            url = new URL(ub.toString());
            String urlParameters = "user=" + uri[1] + "&" + "passw=" + uri[2];
            //might not work for older versions
            byte[] postData = urlParameters.getBytes(UTF_8);
            int postDataLength = postData.length;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("charset", "utf-8");
            urlConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            urlConnection.setUseCaches(false);
            try (DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream())) {
                wr.write(postData);
            }
            //urlConnection.connect();
            InputStream inputStream = null;
            inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
                response = buffer.toString();

            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        if (response != null)
            Log.i("this is what i get", response);

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        //    super.onPostExecute(result);
        //     if (result != null)
        Log.i("this is the result", "HULA");
        //Do anything with response..
    }
}



