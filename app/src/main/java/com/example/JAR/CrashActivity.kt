package com.example.JAR

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.JAR.databinding.ActivityCrashBinding

class CrashActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCrashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Error"
        binding.txtCrashTitle.text = intent.extras?.get("title") as CharSequence?
        binding.txtCrashMessage.text = intent.extras?.get("message") as CharSequence?
        binding.btnOpenTOML.setOnClickListener {
            NavigationHandler.openTomlEditor(this)
        }
        binding.btnFrontpage.setOnClickListener {
            NavigationHandler.openFrontpage(this)
            finish()
        }
    }


}