package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
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