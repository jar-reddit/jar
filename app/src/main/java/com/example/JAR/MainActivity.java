package com.example.JAR;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;


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
        AndroidNetworking.get("https://www.reddit.com/r/frontpage/.json")
                .setUserAgent("android:com.example.JAR:v0.0.1 (by /u/mueea001)")
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                TextView out = findViewById(R.id.test_text);
                try {
                    String outText =  "length: " + response.length() + "\n";
                    for(int i = 0; i < response.length(); i++) {
                        outText += "title: " + response.getJSONObject("data").getJSONArray("children").getJSONObject(i).getJSONObject("data").getString("title");
                        outText += "\nselftext: " + response.getJSONObject("data").getJSONArray("children").getJSONObject(i).getJSONObject("data").getString("selftext") + "\n";
                    }
                    out.setText(outText);
                } catch (JSONException e) {
                    out.setText("JSON error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {

            }
        })
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        TextView out = findViewById(R.id.test_text);
//                        out.setText(response);
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        testText = anError.toString();
//                    }
//                }
//                )
        ;



        testView.setText("please wait");


    }
}
