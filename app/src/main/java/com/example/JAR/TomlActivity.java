package com.example.JAR;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.JAR.databinding.ActivityTomlBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TomlActivity extends AppCompatActivity {
    private ActivityTomlBinding view;
    private File file;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityTomlBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        file = new File(this.getFilesDir(), "settings.toml");
        try {
            Scanner scanner = new Scanner(this.openFileInput(file.getName()));
            String settings = "";
            while (scanner.hasNextLine()) {
                settings += scanner.nextLine()+"\n";
            }
            view.tomlText.setText(settings);
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
