package com.example.android.momintuition;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rpodiuc on 8/26/15.
 */
public class FormatData {
    JSONObject data;
    static int len;
    FormatData(JSONObject jo, String[][] dataPop) throws JSONException {
        data = jo;

        JSONArray s = (JSONArray) data.get("results");
        len = s.length();
        Log.i("length of array", s.length() + "");
        for (int j = 0; j < s.length()-1; j++) {
            JSONObject x = (JSONObject) s.get(j);
            dataPop[j][0] = x.get("name") + "";
            dataPop[j][1] = x.get("types") + "";
            dataPop[j][2] = x.get("place_id") + "";
            dataPop[j][3] = ((JSONObject)(((JSONObject) x.get("geometry")).get("location"))).get("lat")+","+
                            ((JSONObject)(((JSONObject) x.get("geometry")).get("location"))).get("lng")+"";
        }

    }

}
