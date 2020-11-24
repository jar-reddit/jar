package com.example.JAR;

import android.os.FileObserver;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

//Need to figure out how we're formatting the settings file
public class Settings extends AppCompatActivity {
    //Example settings as placeholders
    File settings;
    private Boolean autoplay_video = false;
    private Boolean dark_mode = false;
    private Boolean allow_dms = true;

    /***
     *
     */
    public Settings(){
        /*settings = new File("settings.txt");
        if(settings.exists()){
            readFile();
        }else{
            try {
                defaultSettings();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    /***
     *
     */
    public void writeFile(){
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("settings.txt", MODE_PRIVATE);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets information from existing settings.txt file and
     * initialises the variables
     */
    public void readFile(){

    }

    /***
     * Set default settings when first using the app or
     * when resetting the settings through an option
     */
    public void defaultSettings() throws IOException{
        try {
            //Delete existing settings file
            /*if(settings.exists()){
                settings.delete();
            }*/

            //TODO: This is test code - remove


            //Create settings file and write default settings
            FileOutputStream fos = null;
            try {
                fos = openFileOutput("settings.txt", MODE_PRIVATE);
                //Example default settings
                String l1 = "testing testing 1 2 3";
                fos.write(l1.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally{
                if(fos != null){
                    fos.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
