package com.github.jarforreddit.RAW;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Reddit Data Type used to paginate content
 */
public class Listing {
    private String before; //
    private String after;
    private String modhash;
    private ArrayList<Thing> children;

    public Listing(JSONObject data) throws JSONException {
        before = data.getString("before");
        after = data.getString("after");
        modhash = data.getString("modhash");
        JSONArray jsonChildren = data.getJSONArray("children");
        children = new ArrayList<Thing>();
        int dist = data.getInt("dist");
        for (int i = 0; i < dist; i++) {
            children.add(Thing.getInstance(jsonChildren.getJSONObject(i)));
            Log.d("JAR for Reddit","added children");
        }
    }

    public String getBefore() {
        return before;
    }

    public String getAfter() {
        return after;
    }

    public String getModhash() {
        return modhash;
    }

    public ArrayList<Thing> getChildren() {
        return children;
    }

}
