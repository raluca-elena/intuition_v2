package com.example.android.momintuition;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class DirectionsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    MapView mapView;
    GoogleMap map;
    static boolean crapyFlag = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        AccessibilityManager accessibilityManager =
                (AccessibilityManager) getApplicationContext().getSystemService(Context.ACCESSIBILITY_SERVICE);

        logInstalledandEnabledAccessiblityServices(getApplicationContext());
        if (!isAccessibilityEnabled(getApplicationContext(), "com.example.android.momtuition/com.example.android.momintuition.GMapListener")) {
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivityForResult(intent, 0);
        } else {
            Log.i("---ACCES IS ENABLED", accessibilityManager.isEnabled() + "");
        }

        findViewById(R.id.arrow).setOnClickListener(this);

    }


    public static void logInstalledandEnabledAccessiblityServices(Context context) {

        AccessibilityManager am = (AccessibilityManager) context
                .getSystemService(Context.ACCESSIBILITY_SERVICE);

        List<AccessibilityServiceInfo> runningServices = am
                .getInstalledAccessibilityServiceList();
        for (AccessibilityServiceInfo service : runningServices) {
            Log.i("ACCES SERVICE RUNNING", service.getId());

        }

        List<AccessibilityServiceInfo> enabledServices = am
                .getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK);
        for (AccessibilityServiceInfo service : enabledServices) {
            Log.i("ACCES SERVICE ENABLED", service.getId());
        }
    }


    public static boolean isAccessibilityEnabled(Context context, String id) {

        AccessibilityManager am = (AccessibilityManager) context
                .getSystemService(Context.ACCESSIBILITY_SERVICE);

        List<AccessibilityServiceInfo> runningServices = am
                .getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK);
        for (AccessibilityServiceInfo service : runningServices) {
            if (id.equals(service.getId())) {
                return true;
            }
        }

        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.map = googleMap;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.65331,-79.38277), 5);
        map.animateCamera(cameraUpdate);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=AIzaSyDISYkoIYzQesb1VQ0eQQ6x0BaWxD87xWg";
        //url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&key=AIzaSyBbE2wO2MDZ2goETgsY__ifEq2dlOMLLc4";
        JsonObjectRequest stringRequest = new JsonObjectRequest(JsonObjectRequest.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        drawPath(response);
                    }} , new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("this is the error", error + "");
                        }
        });
        int MY_SOCKET_TIMEOUT_MS = 9000;
        Log.i("max retries", DefaultRetryPolicy.DEFAULT_MAX_RETRIES + "");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);



    }


    public void drawPath(JSONObject response){
        JSONArray routeArray = null;
        try {
            routeArray = response.getJSONArray("routes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject routes = null;
        try {
            routes = routeArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject overviewPolylines = null;
        try {
            overviewPolylines = routes.getJSONObject("overview_polyline");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String encodedString = null;
        try {
            encodedString = overviewPolylines.getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<LatLng> list = decodePoly(encodedString);

        Polyline line = map.addPolyline(new PolylineOptions()
                        .addAll(list)
                        .width(12)
                        .color(Color.parseColor("#05b1fb"))//Google maps blue color
                        .geodesic(true)
        );

    }

    @Override
    public void onResume() {
        mapView.onResume();
        Log.i("OnResume", "why onresume happens now?");
        super.onResume();
    }

            @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=45.7062727,27.1826593&daddr=45.7066196,27.1826913"));
        startActivityForResult(intent, 0);
        crapyFlag = true;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("ON ACTIVITY", " Request code " + requestCode);
        Log.i("ON ACTIVITY", "result code " + resultCode);
        if (crapyFlag) {
            Intent intent = new Intent(getApplicationContext(), ActivityChooser.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            crapyFlag = false;
        }
    }
}
