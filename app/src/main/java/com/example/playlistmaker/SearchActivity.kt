package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val searchToolbar: Toolbar = binding.searchToolbar
        searchToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back)
        searchToolbar.setNavigationOnClickListener { finish() }

        val searchBar: EditText = binding.searchBar
        val searchBarTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }

            override fun afterTextChanged(p0: Editable?) {
                //TODO("Not yet implemented")
            }
        }
        searchBar.addTextChangedListener(searchBarTextWatcher)
    }
}