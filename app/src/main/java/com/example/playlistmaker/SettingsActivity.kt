package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val settingsToolbar: Toolbar = binding.settingsToolbar
        settingsToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back)
        settingsToolbar.setNavigationOnClickListener { finish() }

        val shareButton = binding.share
        shareButton.setOnClickListener {onShareButtonClick(getString(R.string.settings_screen_item_share_link))}
    }

    private fun onShareButtonClick(text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}