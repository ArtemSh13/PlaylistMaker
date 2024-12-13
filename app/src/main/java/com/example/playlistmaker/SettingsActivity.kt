package com.example.playlistmaker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val settingsToolbar: Toolbar = findViewById(R.id.settings_toolbar)
        settingsToolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        settingsToolbar.setNavigationOnClickListener { finish() }
    }
}