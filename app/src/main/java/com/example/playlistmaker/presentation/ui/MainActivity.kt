package com.example.playlistmaker.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.playlistmaker.data.repositories.SharedPreferencesKeeper
import com.example.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        SharedPreferencesKeeper.initSharedPreferencesFromContext(this)
        AppCompatDelegate.setDefaultNightMode(
            if (SharedPreferencesKeeper.getDarkThemeSwitchState()) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }
        setContentView(binding.root)

        val searchButtonClickListener: View.OnClickListener = View.OnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    SearchActivity::class.java
                )
            )
        }
        binding.searchButton.setOnClickListener(searchButtonClickListener)

        val libraryButtonClickListener: View.OnClickListener = View.OnClickListener {
            startActivity(Intent(this, LibraryActivity::class.java))
        }
        binding.libraryButton.setOnClickListener(libraryButtonClickListener)

        val settingsButtonClickListener: View.OnClickListener = View.OnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.settingsButton.setOnClickListener(settingsButtonClickListener)
    }
}