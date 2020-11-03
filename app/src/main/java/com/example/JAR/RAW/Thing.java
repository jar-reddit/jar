package com.example.JAR.RAW;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A Reddit base class
 * https://github.com/reddit-archive/reddit/wiki/JSON
 */
public class Thing {
    private String id;
    private String name;
    private String kind;
    private Object data;

    public Thing(JSONObject data) throws JSONException {
        id = data.getJSONObject("data").getString("id");
        name = data.getJSONObject("data").getString("name");
        kind = data.getString("kind");
    }

    public static Thing getInstance(JSONObject data) throws JSONException {
        Thing inst = null;
        Log.d("JAR", data.getString("kind"));
        if (data.getString("kind").equals("t3")) {
            Log.d("JAR","kind=t3");
            return new Submission(data);
        }
        return inst;        
    }
}
