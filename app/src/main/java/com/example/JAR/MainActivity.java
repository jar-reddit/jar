package com.example.JAR;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.JAR.RAW.Reddit;


import org.json.JSONException;
import org.json.JSONObject;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidNetworking.initialize(getApplicationContext());

        testView = findViewById(R.id.test_text);
        //TODO: make our own Reddit API Wrapper
//        AndroidNetworking.get("https://www.reddit.com/.json")
//                .setUserAgent("android:com.example.JAR:v0.0.1 (by /u/mueea001)")
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
        Reddit reddit = new Reddit(getApplicationContext(),"android:com.example.JAR:v0.0.1 (by /u/JARForReddit)");
        reddit.getFrontpage();


        testView.setText("please wait");


    }
}
