package com.example.JAR;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.JAR.RAW.Reddit;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

// Emilio comment
// Magd comment
// Murray Comment
// Steve Comment
public class MainActivity extends AppCompatActivity {
    public String testText; // TODO: remove these public variables
    public TextView testView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: 27/10/20 remove this policy  https://developer.android.com/reference/android/os/NetworkOnMainThreadException
        // https://stackoverflow.com/a/9289190
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidNetworking.initialize(getApplicationContext());


        testView = findViewById(R.id.test_text);
        //TODO: make our own Reddit API Wrapper
//        AndroidNetworking.get("https://www.reddit.com/.json")
//                .setUserAgent("android:com.example.JAR:v0.0.1 (by /u/JARForReddit)")
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        TextView out = findViewById(R.id.test_text);
//                        try {
//
//                            int length = response.getJSONObject("data").getInt("dist");
//                            String outText = "length: " + length + "\n";
//                            for (int i = 0; i < length; i++) {
//                                outText += "\nsub: " + response.getJSONObject("data").getJSONArray("children").getJSONObject(i).getJSONObject("data").getString("subreddit");
//                                outText += "\ntitle: " + response.getJSONObject("data").getJSONArray("children").getJSONObject(i).getJSONObject("data").getString("title");
//                                outText += "\nselftext: " + response.getJSONObject("data").getJSONArray("children").getJSONObject(i).getJSONObject("data").getString("selftext") + "\n";
//                            }
//                            out.setText(outText);
//                        } catch (JSONException e) {
//                            out.setText("JSON error");
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//
//                    }
//                });
        testView.setText("please wait");

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://www.reddit.com/.json").openConnection();
            connection.setRequestMethod("GET");
            //connection.setRequestProperty("User Agent",getApplicationContext().getPackageName()+":v0.0.1 (by /u/JARForReddit)");
            Scanner s = new Scanner((InputStream)connection.getContent());
            String response = "";
            while (s.hasNext()) {
                response += s.nextLine();
            }
            JSONObject json = new JSONObject(response);
            testView.setText(getApplicationContext().getPackageName()+":v0.0.1 (by /u/JARForReddit)");
        } catch (IOException | JSONException e) {
            testView.setText(e.toString());
            e.printStackTrace();
        }


//        Reddit reddit = new Reddit(getApplicationContext(),"android:com.example.JAR:v0.0.1 (by /u/JARForReddit)");
//        reddit.getFrontpage();





    }
}
