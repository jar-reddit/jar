package com.example.JAR;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import net.dean.jraw.models.internal.GenericJsonResponse;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Settings{
    private static Settings INSTANCE = null;
    //Example settings as placeholders
    private Toml setting;
    private Boolean autoplay_video = false;
    private Boolean dark_mode = false;
    private Boolean allow_dms = true;
    private Context context;
    private HashMap<String, String> settings;
    private File settingFile;

    public Settings(Context context){
        this.context = context;
        settings = new HashMap<>();
        settingFile = new File(context.getFilesDir(),"settings.toml");

        if(!settingFile.exists()){
//            readSettings();
            defaultToml();
        }
        setting = new Toml().read(settingFile);
    }

    /***
     * Overwrite settings in setting.txt with current settings
     */
    public void writeFile(){
        try {
            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput("settings.txt", Context.MODE_PRIVATE);
                //Example user settings
                JSONObject settingsJson = new JSONObject(settings);

                String l1 = "autoplay:"+settings.get("autoplay")+'\n'+"darkmode:"+settings.get("darkmode")+'\n'+"allow_dms:"+settings.get("allow_dms");
//                fos.write(l1.getBytes());
                fos.write(settingsJson.toString().getBytes(StandardCharsets.UTF_8));


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

    public static Settings getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Settings(context);
        }
        return INSTANCE;
    }

    private void defaultToml() {
        TomlWriter tomlWriter = new TomlWriter();
        try {

            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(settingFile.getName(),Context.MODE_PRIVATE);
//                fos = context.openFileOutput("settings.txt", context.MODE_PRIVATE);
                //Example default settings
                String l1 = "autoplay:false"+'\n'+"darkmode:false"+'\n'+"allow_dms:true";
                settings.put("autoplay","false");
                settings.put("darkmode","false");
                settings.put("allow_dms","true");
                settings.put("separator","\uD83C\uDDF5\uD83C\uDDF0");
                settings.put("format","r/$subreddit | u/$username | $flair");
                tomlWriter.write(settings,fos);
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
    
    public static Toml getSettings(Context context) {
        return getInstance(context).setting;
    }
    
    public static void refresh(){
        INSTANCE = new Settings(INSTANCE.context);
    }

}
