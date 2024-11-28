package com.example.playlistmaker

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
        val searchButtonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(
                    this@MainActivity, "Нажали на кнопку \"Поиск\"",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        searchButton.setOnClickListener(searchButtonClickListener)

        val libraryButton = findViewById<Button>(R.id.library_button)
        val libraryButtonClickListener: View.OnClickListener = View.OnClickListener {
            Toast.makeText(
                this@MainActivity, "Нажали на кнопку \"Медиатека\"",
                Toast.LENGTH_SHORT
            ).show()
        }
        libraryButton.setOnClickListener(libraryButtonClickListener)

        val settingsButton = findViewById<Button>(R.id.settings_button)
        val settingsButtonClickListener: View.OnClickListener = View.OnClickListener {
            Toast.makeText(
                this@MainActivity, "Нажали на кнопку \"Настройки\"",
                Toast.LENGTH_SHORT
            ).show()
        }
        settingsButton.setOnClickListener(settingsButtonClickListener)
    }
}