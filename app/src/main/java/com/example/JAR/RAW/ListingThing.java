package com.example.JAR.RAW;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ListingThing {
    private String kind;
    private Listing data;

    public ListingThing(JSONObject json) throws JSONException {
        kind = json.getString("kind");
        data = new Listing(json.getJSONObject("data"));
    }

    public String getKind() {
        return kind;
    }

    public Listing getData() {
        return data;
    }
}
