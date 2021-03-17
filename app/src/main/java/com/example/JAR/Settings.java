package com.example.JAR;

import android.content.Context;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

public class Settings {
    private static Settings INSTANCE = null;
    //Example settings as placeholders
    private final Toml setting;
    private final Boolean autoplay_video = false;
    private final Boolean dark_mode = false;
    private final Boolean allow_dms = true;
    private final Context context;
    private final HashMap<String, String> settings;
    private final File settingFile;

    public Settings(Context context) {
        this.context = context;
        settings = new HashMap<>();
        settingFile = new File(context.getFilesDir(), "settings.toml");

        if (!settingFile.exists()) {
//            readSettings();
            defaultToml();
        }
        setting = new Toml().read(settingFile);
    }

    public static Settings getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Settings(context);
        }
        return INSTANCE;
    }

    public static Toml getSettings(Context context) {
        return getInstance(context).setting;
    }

    public static void refresh() {
        INSTANCE = new Settings(INSTANCE.context);
    }

    /***
     * Overwrite settings in setting.txt with current settings
     */
    public void writeFile() {
        try {
            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput("settings.txt", Context.MODE_PRIVATE);
                //Example user settings
                JSONObject settingsJson = new JSONObject(settings);

                String l1 = "autoplay:" + settings.get("autoplay") + '\n' + "darkmode:" + settings.get("darkmode") + '\n' + "allow_dms:" + settings.get("allow_dms");
//                fos.write(l1.getBytes());
                fos.write(settingsJson.toString().getBytes(StandardCharsets.UTF_8));


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        } catch (IOException e) {
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
                fos = context.openFileOutput("settings.txt", Context.MODE_PRIVATE);
                //Example default settings
                String l1 = "autoplay:false" + '\n' + "darkmode:false" + '\n' + "allow_dms:true";
                settings.put("autoplay", "false");
                settings.put("darkmode", "false");
                settings.put("allow_dms", "true");
                fos.write(l1.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        } catch (IOException e) {
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
                    option = text.substring(0, text.indexOf(":"));
                    value = text.substring(text.indexOf(":"));
                    text = br.readLine();
//                    settings.replace(option, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void defaultToml() {
        try {

                CustomStringBuilder txtSettings = new CustomStringBuilder();
                Writer writer = new FileWriter(settingFile);
                Scanner scanner = new Scanner(context.getAssets().open("settings.toml"));
            while (scanner.hasNextLine()) {
                txtSettings.appendLine(scanner.nextLine());
            }
            scanner.close();
                writer.write(txtSettings.toString());
                writer.close();
                refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    class CustomStringBuilder {
        StringBuilder builder;
        public CustomStringBuilder() {
            builder = new StringBuilder();
        }

        public CustomStringBuilder appendLine() {
            return appendLine("");
        }
        
        public CustomStringBuilder appendLine(String str) {
            builder.append(str).append("\n");
            return this;
        }

        public CustomStringBuilder newLine() {
            builder.append("\n");
            return this;
        }

        @Override
        public String toString() {
            return builder.toString();
        }
    }

}
