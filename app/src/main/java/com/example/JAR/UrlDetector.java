package com.example.JAR;

import android.util.Log;

import net.dean.jraw.models.Sorting;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The {@link UrlDetector} class is used to handle the detection of URLs
 */
public class UrlDetector {

    /**
     * This method will try to detect the type of the given url
     * @param url a url
     * @return the type of the url
     */
    public static String detect(URL url) {
        Log.d("Detect URL", url.toString());
        switch (url.getAuthority()) {
            case "i.redd.it":
                return "reddit:image";
            case "v.redd.it":
                return "reddit:video";
            case "i.imgur.com":
                Log.d("Imgur",url.getPath());
                if (url.getPath().endsWith(".gifv")||url.getPath().endsWith(".mp4")) {
                    return "imgur:video";
                } else{
                    return "imgur:image";
                }
        }
        return "unknown";
    }
    
    public static String detect(String url) {
        try {
            return detect(new URL(url));
        } catch (MalformedURLException e) {
            Log.d("JAR/URL",e.getMessage());
        }
        return "bad url";
    }
}
