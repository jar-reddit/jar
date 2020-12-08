package com.example.JAR;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Settings extends AppCompatActivity {
    //Example settings as placeholders
    private Boolean autoplay_video = false;
    private Boolean dark_mode = false;
    private Boolean allow_dms = true;
    private Context context;
    private HashMap<String, String> settings;

    public Settings(Context context){
        this.context = context;
        settings = new HashMap<>();
        File file = new File(context.getFilesDir(),"settings.txt");
        if(file.exists()){
            readSettings();
        }else{
            defaultSettings();
        }
    }

    /***
     * Overwrite settings in setting.txt with current settings
     */
    public void writeFile(){
        try {
            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput("settings.txt", context.MODE_PRIVATE);
                //Example user settings
                String l1 = "autoplay:"+settings.get("autoplay")+'\n'+"darkmode:"+settings.get("darkmode")+'\n'+"allow_dms:"+settings.get("allow_dms");
                fos.write(l1.getBytes());
            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /***
     * Set default settings when first using the app or
     * when resetting the settings through an option
     */
    public void defaultSettings() {
        try {
            settings.clear();
            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput("settings.txt", context.MODE_PRIVATE);
                //Example default settings
                String l1 = "autoplay:false"+'\n'+"darkmode:false"+'\n'+"allow_dms:true";
                settings.put("autoplay","false");
                settings.put("darkmode","false");
                settings.put("allow_dms","true");
                fos.write(l1.getBytes());
            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Gets information from existing settings.txt file and
     * replaces their values in the settings map
     */
    public void readSettings() {
        try {
            FileInputStream fis = null;
            try {
                fis = context.openFileInput("settings.txt");
                InputStreamReader reader = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(reader);
                String text = br.readLine();
                String option = "";
                String value = "";
                while (text != null) {
                    option = text.substring(0,text.indexOf(":"));
                    value = text.substring(text.indexOf(":"), text.length());
                    text = br.readLine();
                    settings.replace(option, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally{
                if(fis != null){
                    fis.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
