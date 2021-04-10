package com.github.jarforreddit;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.jarforreddit.databinding.ActivityTomlBinding;
import com.moandjiezana.toml.Toml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

public class TomlActivity extends AppCompatActivity {
    private ActivityTomlBinding view;
    private File file;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityTomlBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        setTitle("TOML");
        file = new File(this.getFilesDir(), "settings.toml");

        readToml();
        
        view.btnApply.setOnClickListener((view1)->{
            try {
                Toml t = new Toml();
                t.read(view.tomlText.getText().toString());
                Writer writer = new FileWriter(file);
                writer.write(view.tomlText.getText().toString());
                writer.close();
                Settings.refresh();
                TomlActivity.this.finish();
            } catch (IOException | IllegalStateException  e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
        
        view.btnCancel.setOnClickListener((view)->{
            TomlActivity.this.finish();
        });

        view.btnReset.setOnClickListener((btnView)->{
            Settings.getInstance(this).defaultToml(true);
            readToml();
        });
    }
    
    private void readToml() {
        try {
            Scanner scanner = new Scanner(this.openFileInput(file.getName()));
            StringBuilder settings = new StringBuilder();
            while (scanner.hasNextLine()) {
                settings.append(scanner.nextLine()).append("\n");
            }
            view.tomlText.setText(settings.toString());
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
