package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private var searchBarInputTextValue = INPUT_TEXT_DEF

    companion object {
        const val SEARCH_BAR_INPUT_TEXT = "SEARCH_BAR_INPUT_TEXT"
        const val INPUT_TEXT_DEF = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }
        setContentView(binding.root)
        SharedPreferencesKeeper.initSharedPreferencesFromContext(this)

        fun showTrackList() {
            binding.stub.visibility = View.GONE
            binding.searchScreenStubUpdateButton.visibility = View.GONE

            binding.trackList.visibility = View.VISIBLE
        }

        fun clearTrackList() { binding.trackList.adapter = TrackAdapter(tracks = emptyList()) }

        fun showNothingFoundStub() {
            binding.trackList.visibility = View.GONE
            binding.searchScreenStubUpdateButton.visibility = View.GONE

            binding.stubPrimaryText.setText(R.string.search_screen_stub_nothing_found_primary_text)
            binding.stubSecondaryText.setText(R.string.search_screen_stub_nothing_found_secondary_text)
            binding.stubImage.setImageResource(R.drawable.img_nothing_found)

            binding.stub.visibility = View.VISIBLE
        }

        fun showConnectionProblemStub() {
            binding.trackList.visibility = View.GONE

            binding.stubPrimaryText.setText(R.string.search_screen_stub_connection_problem_primary_text)
            binding.stubSecondaryText.setText(R.string.search_screen_stub_connection_problem_secondary_text)
            binding.stubImage.setImageResource(R.drawable.img_connection_problem)
            binding.searchScreenStubUpdateButton.visibility = View.VISIBLE

            binding.stub.visibility = View.VISIBLE
        }

        fun showTracksHistory() {
            val tracksHistory = SharedPreferencesKeeper.getTracksHistory()
            if (tracksHistory.isNotEmpty()) {
                binding.trackList.visibility = View.GONE
                binding.stub.visibility = View.GONE
                binding.trackHistoryTrackList.adapter = TrackAdapter(tracks = tracksHistory)
                binding.trackHistory.visibility = View.VISIBLE
            }
        }

        fun hideTracksHistory() {
            clearTrackList()
            binding.trackList.visibility = View.VISIBLE
            binding.stub.visibility = View.GONE
            binding.trackHistory.visibility = View.GONE
        }

        // Toolbar
        binding.searchToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back)
        binding.searchToolbar.setNavigationOnClickListener { finish() }

        // Searchbar
        binding.clearSearchBarButton.setOnClickListener {
            binding.searchBar.text.clear()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
            clearTrackList()
            showTrackList()
        }

        val searchBarTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearSearchBarButton.isVisible = !s.isNullOrEmpty()
                if (s.isNullOrEmpty()) {
                    clearTrackList()
                    showTrackList()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                searchBarInputTextValue = binding.searchBar.text.toString()
                if (binding.searchBar.text.isEmpty()) {
                    showTracksHistory()
                } else {
                    hideTracksHistory()
                }
            }
        }
        binding.searchBar.addTextChangedListener(searchBarTextWatcher)
        binding.trackHistoryTrackList.layoutManager = LinearLayoutManager(this)
        binding.searchBar.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchBar.text.isEmpty()) {
                showTracksHistory()
            } else {
                hideTracksHistory()
            }
        }

        // Track list
        binding.trackList.layoutManager = LinearLayoutManager(this)

        val callbackiTunesAPIService = object : Callback<iTunesResponse>{
            override fun onResponse(call: Call<iTunesResponse>, response: Response<iTunesResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.resultCount > 0) {
                        showTrackList()
                        val responseTracks = response.body()?.results.orEmpty()
                        binding.trackList.adapter = TrackAdapter(tracks = responseTracks)
                    } else {
                        showNothingFoundStub()
                    }
                } else {
                    showConnectionProblemStub()
                }
            }

            override fun onFailure(call: Call<iTunesResponse>, t: Throwable) {
                t.printStackTrace()
                showConnectionProblemStub()
            }
        }

        binding.searchScreenStubUpdateButton.setOnClickListener { iTunesAPIService.instance.getSongsByTerm(binding.searchBar.text.toString())
            .enqueue(callbackiTunesAPIService)
        }

        binding.clearTracksHistoryButton.setOnClickListener {
            hideTracksHistory()
            SharedPreferencesKeeper.clearTracksHistory()
        }

        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && binding.searchBar.text.isNotEmpty()) {
                iTunesAPIService.instance.getSongsByTerm(binding.searchBar.text.toString())
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