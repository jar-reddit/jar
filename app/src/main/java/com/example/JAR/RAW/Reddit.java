package com.example.JAR.RAW;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.gsonparserfactory.GsonParserFactory;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import static android.content.ContentValues.TAG;


public class Reddit {
    private final String url ="https://www.reddit.com/";
    String params;
    /**
     * Start a Reddit instance
     * // TODO: 15/10/20 Better Doc
     * @param context Application context
     * @param useragent The useragent string needed for Reddit
     */
    public Reddit(Context context, String useragent) {
        AndroidNetworking.initialize(context);
        AndroidNetworking.setUserAgent(useragent);
        AndroidNetworking.setParserFactory(new GsonParserFactory());
        params = "raw_json=1";
    }

    /**
     * Gets frontpage of Reddit
     */
    public JSONObject getFrontpage() {
        JSONObject testListing = null; // TODO: 19/10/20 Remove this later
        AndroidNetworking
                .get(url + "?" + params)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }

                    @Override
                    public void onError(ANError anError) {

                    }

                    public JSONObjectRequestListener init(JSONObject listing) {
                        return this;
                    }
                }.init(testListing));
        return testListing;
    }
}
