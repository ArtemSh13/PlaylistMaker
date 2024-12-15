package com.example.playlistmaker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val settingsToolbar: Toolbar = findViewById(R.id.settings_toolbar)
        settingsToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back)
        settingsToolbar.setNavigationOnClickListener { finish() }
    }
}