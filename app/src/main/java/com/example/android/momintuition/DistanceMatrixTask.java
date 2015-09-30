package com.example.android.momintuition;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rpodiuc on 9/29/15.
 */
public class DistanceMatrixTask{

    Context context;
    String[][] coord;
    RequestQueue queue;

    DistanceMatrixTask(Context c, String[][] coord){
        context = c;
        queue = Volley.newRequestQueue(c);
        this.coord = coord;
        for (int i = 0; i< coord.length; i++){
            request(coord[i][3]);
        }
    }

    String url ="https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
    String homecoord = "37.4031455,-122.0753819";
    //url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&key=AIzaSyBbE2wO2MDZ2goETgsY__ifEq2dlOMLLc4";


    void request(String coords){
        String uri = url + homecoord+  "&destinations=" + coords + "&sensor=false&key=AIzaSyDISYkoIYzQesb1VQ0eQQ6x0BaWxD87xWg";
        JsonObjectRequest stringRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, uri,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        Log.i("Distance response", response + "");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("this is the error", error + "");
            }
        });
        int  MY_SOCKET_TIMEOUT_MS = 9000;
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }





}
