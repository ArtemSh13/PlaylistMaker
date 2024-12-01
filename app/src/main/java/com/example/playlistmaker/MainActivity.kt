package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val searchButtonClickListener: View.OnClickListener = View.OnClickListener { startActivity(Intent(this@MainActivity, SearchActivity::class.java)) }
        searchButton.setOnClickListener(searchButtonClickListener)

        val libraryButton = findViewById<Button>(R.id.library_button)
        val libraryButtonClickListener: View.OnClickListener = View.OnClickListener {
            startActivity(Intent(this, LibraryActivity::class.java))
        }
        libraryButton.setOnClickListener(libraryButtonClickListener)

        val settingsButton = findViewById<Button>(R.id.settings_button)
        val settingsButtonClickListener: View.OnClickListener = View.OnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        settingsButton.setOnClickListener(settingsButtonClickListener)
    }
}