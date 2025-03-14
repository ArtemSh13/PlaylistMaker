package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesAPIService = retrofit.create<iTunesAPI>()

    private var searchBarInputTextValue = INPUT_TEXT_DEF

    companion object {
        const val SEARCH_BAR_INPUT_TEXT = "SEARCH_BAR_INPUT_TEXT"
        const val INPUT_TEXT_DEF = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.searchToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back)
        binding.searchToolbar.setNavigationOnClickListener { finish() }

        binding.clearSearchBarButton.setOnClickListener {
            binding.searchBar.text.clear()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        val searchBarTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearSearchBarButton.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {
                searchBarInputTextValue = binding.searchBar.text.toString()
            }
        }
        binding.searchBar.addTextChangedListener(searchBarTextWatcher)

        binding.trackList.layoutManager = LinearLayoutManager(this)

        val callbackiTunesAPIService = object : Callback<iTunesResponse>{
            override fun onResponse(call: Call<iTunesResponse>, response: Response<iTunesResponse>) {
                // Получили ответ от сервера
                if (response.isSuccessful) {
                    // Наш запрос был удачным, получаем наши объекты
                    if (response.body()!!.resultCount > 0) {
                        binding.stub.visibility = View.GONE
                        binding.searchScreenStubUpdateButton.visibility = View.GONE

                        binding.trackList.visibility = View.VISIBLE
                        val responseTracks = response.body()?.results.orEmpty()
                        binding.trackList.adapter = TrackAdapter(tracks = responseTracks)
                    } else {
                        binding.trackList.visibility = View.GONE
                        binding.searchScreenStubUpdateButton.visibility = View.GONE

                        binding.stubPrimaryText.setText(R.string.search_screen_stub_nothing_found_primary_text)
                        binding.stubSecondaryText.setText(R.string.search_screen_stub_nothing_found_secondary_text)
                        binding.stubImage.setImageResource(R.drawable.im_nothing_found)
                        binding.stub.visibility = View.VISIBLE
                    }
                } else {
                    // Сервер отклонил наш запрос с ошибкой
                    binding.trackList.visibility = View.GONE

                    binding.stubPrimaryText.setText(R.string.search_screen_stub_connection_problem_primary_text)
                    binding.stubSecondaryText.setText(R.string.search_screen_stub_connection_problem_secondary_text)
                    binding.stubImage.setImageResource(R.drawable.im_connection_problem)
                    binding.searchScreenStubUpdateButton.visibility = View.VISIBLE
                    binding.stub.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<iTunesResponse>, t: Throwable) {
                // Не смогли присоединиться к серверу
                // Выводим ошибку в лог, что-то пошло не так
                t.printStackTrace()
            }
        }

        binding.searchScreenStubUpdateButton.setOnClickListener { iTunesAPIService.getSongsByTerm(binding.searchBar.text.toString())
            .enqueue(callbackiTunesAPIService)
        }

        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                iTunesAPIService.getSongsByTerm(binding.searchBar.text.toString())
                    .enqueue(
                        callbackiTunesAPIService
                    )
                true
            }
            false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_BAR_INPUT_TEXT, searchBarInputTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedInstanceStateValue = savedInstanceState.getString(SEARCH_BAR_INPUT_TEXT)
        if (savedInstanceStateValue != null) {
            searchBarInputTextValue = savedInstanceStateValue
        }
    }
}