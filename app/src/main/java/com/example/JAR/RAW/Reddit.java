package com.example.JAR.RAW;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.gsonparserfactory.GsonParserFactory;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;


public class Reddit {
    private final String url ="https://www.reddit.com/";
    String ua;
    String params;
    /**
     * Start a Reddit instance
     * // TODO: 15/10/20 Better Doc
     * @param context Application context
     * @param useragent The useragent string needed for Reddit
     */
    public Reddit(Context context, String useragent) {
        ua = useragent;
        params = "raw_json=1";
    }

    /**
     * Gets frontpage of Reddit
     */
    public ListingThing getFrontpage() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url+".json?"+params).openConnection();
            connection.addRequestProperty("User-Agent",ua);
            connection.setRequestMethod("GET");
            Scanner s = new Scanner((InputStream)connection.getContent());

            String response = "";
            while (s.hasNext()) {
                response += s.nextLine();
            }
            JSONObject json = new JSONObject(response);

            return new ListingThing(json);
        } catch (IOException | JSONException e) {
            // TODO: 28/10/20 Malformed URL Exception
            e.printStackTrace();
            Log.e("JAR for Reddit", e.getMessage());
        }
        return null;
    }
}
