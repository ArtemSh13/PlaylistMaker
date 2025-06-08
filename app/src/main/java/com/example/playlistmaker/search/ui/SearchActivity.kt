package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.repositories.SharedPreferencesKeeper
import com.example.playlistmaker.search.domain.entities.Track
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.domain.api.TracksInteractor

enum class SearchActivityState(val value: Map<String, Int>) {
    DEFAULT(
        mapOf(
            "progressBar" to View.GONE,
            "trackList" to View.GONE,
            "trackHistory" to View.GONE,
            "stub" to View.GONE,
            "stubImage" to View.GONE,
            "stubPrimaryText" to View.GONE,
            "stubSecondaryText" to View.GONE,
            "searchScreenStubUpdateButton" to View.GONE
        )
    ),
    TRACK_HISTORY_SHOWING(
        mapOf(
            "progressBar" to View.GONE,
            "trackList" to View.GONE,
            "trackHistory" to View.VISIBLE,
            "stub" to View.GONE,
            "stubImage" to View.GONE,
            "stubPrimaryText" to View.GONE,
            "stubSecondaryText" to View.GONE,
            "searchScreenStubUpdateButton" to View.GONE
        )
    ),
    NOTHING_FOUND(
        mapOf(
            "progressBar" to View.GONE,
            "trackList" to View.GONE,
            "trackHistory" to View.GONE,
            "stub" to View.VISIBLE,
            "stubImage" to View.VISIBLE,
            "stubPrimaryText" to View.VISIBLE,
            "stubSecondaryText" to View.GONE,
            "searchScreenStubUpdateButton" to View.GONE
        )
    ),
    CONNECTION_PROBLEM(
        mapOf(
            "progressBar" to View.GONE,
            "trackList" to View.GONE,
            "trackHistory" to View.GONE,
            "stub" to View.VISIBLE,
            "stubImage" to View.VISIBLE,
            "stubPrimaryText" to View.VISIBLE,
            "stubSecondaryText" to View.VISIBLE,
            "searchScreenStubUpdateButton" to View.VISIBLE
        )
    ),
    LOADING(
        mapOf(
            "progressBar" to View.VISIBLE,
            "trackList" to View.GONE,
            "trackHistory" to View.GONE,
            "stub" to View.GONE,
            "stubImage" to View.GONE,
            "stubPrimaryText" to View.GONE,
            "stubSecondaryText" to View.GONE,
            "searchScreenStubUpdateButton" to View.GONE
        )
    ),
    TRACK_LIST_SHOWING(
        mapOf(
            "progressBar" to View.GONE,
            "trackList" to View.VISIBLE,
            "trackHistory" to View.GONE,
            "stub" to View.GONE,
            "stubImage" to View.GONE,
            "stubPrimaryText" to View.GONE,
            "stubSecondaryText" to View.GONE,
            "searchScreenStubUpdateButton" to View.GONE
        )
    )
}

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    companion object {
        const val SEARCH_BAR_INPUT_TEXT = "SEARCH_BAR_INPUT_TEXT"
        const val INPUT_TEXT_DEF = ""
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var searchBarInputTextValue = INPUT_TEXT_DEF

    private val onTrackClickCallback = { track: Track ->
        startActivity(
            Intent(
                this@SearchActivity,
                AudioPlayerActivity::class.java
            )
                .putExtra("clickedTrack", track)
        )
    }

    private fun clearTrackList() {
        this.binding.trackList.adapter = TrackAdapter(tracks = emptyList(), onTrackClick = { })
    }

    private fun showTracksHistory() {
        val tracksHistory = SharedPreferencesKeeper.getTracksHistory()
        if (tracksHistory.isNotEmpty()) {
            this.binding.trackHistoryTrackList.adapter =
                TrackAdapter(tracks = tracksHistory, onTrackClick = onTrackClickCallback)
            this.setScreenState(SearchActivityState.TRACK_HISTORY_SHOWING)
        }
    }

    private fun hideTracksHistory() {
        this.clearTrackList()
        this.setScreenState(SearchActivityState.TRACK_LIST_SHOWING)
    }

    private val mainHandler = Handler(Looper.getMainLooper())
    private val tracksInteractor = Creator.provideTracksInteractor(this)
    private val searchRunnable = Runnable { searchTracks() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        this.binding = ActivitySearchBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(this.binding.root) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }
        setContentView(this.binding.root)
        SharedPreferencesKeeper.initSharedPreferencesFromContext(this)

        // Toolbar
        this.binding.searchToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back)
        this.binding.searchToolbar.setNavigationOnClickListener { finish() }

        // Searchbar
        this.binding.clearSearchBarButton.setOnClickListener {
            this.binding.searchBar.text.clear()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.binding.root.windowToken, 0)
            this.clearTrackList()
            this.setScreenState(SearchActivityState.TRACK_LIST_SHOWING)
        }

        val searchBarTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearSearchBarButton.isVisible = !s.isNullOrEmpty()
                if (s.isNullOrEmpty()) {
                    clearTrackList()
                    this@SearchActivity.setScreenState(SearchActivityState.TRACK_LIST_SHOWING)
                }
                mainHandler.removeCallbacks(searchRunnable)
            }

            override fun afterTextChanged(p0: Editable?) {
                searchBarInputTextValue = binding.searchBar.text.toString()
                if (binding.searchBar.text.isEmpty()) {
                    showTracksHistory()
                } else {
                    hideTracksHistory()
                    mainHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
                }
            }
        }
        this.binding.searchBar.addTextChangedListener(searchBarTextWatcher)
        this.binding.trackHistoryTrackList.layoutManager = LinearLayoutManager(this)
        this.binding.searchBar.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchBar.text.isEmpty()) {
                showTracksHistory()
            } else {
                hideTracksHistory()
            }
        }

        // Track list
        this.binding.trackList.layoutManager = LinearLayoutManager(this)

        this.binding.searchScreenStubUpdateButton.setOnClickListener {
            this.mainHandler.post(this.searchRunnable)
        }

        this.binding.clearTracksHistoryButton.setOnClickListener {
            hideTracksHistory()
            SharedPreferencesKeeper.clearTracksHistory()
        }

        this.binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && this.binding.searchBar.text.isNotEmpty()) {
                this.mainHandler.post(this.searchRunnable)
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
            this.searchBarInputTextValue = savedInstanceStateValue
        }
    }

    private fun searchTracks() {
        this.setScreenState(SearchActivityState.LOADING)
        tracksInteractor.searchTracks(
            term = binding.searchBar.text.toString(),
            consumer = object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                    mainHandler.post {
                        when (foundTracks) {
                            null -> {
                                this@SearchActivity.setScreenState(SearchActivityState.CONNECTION_PROBLEM)
                            }

                            emptyList<Track>() -> {
                                this@SearchActivity.setScreenState(SearchActivityState.NOTHING_FOUND)
                            }

                            else -> {
                                binding.trackList.adapter = TrackAdapter(
                                    tracks = foundTracks,
                                    onTrackClick = onTrackClickCallback
                                )
                                this@SearchActivity.setScreenState(SearchActivityState.TRACK_LIST_SHOWING)
                            }
                        }
                    }
                }
            }
        )
    }

    private fun setScreenState(state: SearchActivityState) {
        when (state) {
            SearchActivityState.DEFAULT -> {}
            SearchActivityState.TRACK_HISTORY_SHOWING -> {}
            SearchActivityState.NOTHING_FOUND -> {
                this.setContentForNothingFoundState()
            }

            SearchActivityState.CONNECTION_PROBLEM -> {
                this.setContentForConnectionProblemState()
            }

            SearchActivityState.LOADING -> {}
            SearchActivityState.TRACK_LIST_SHOWING -> {}
        }

        this.binding.progressBar.visibility = state.value["progressBar"] ?: View.GONE
        this.binding.trackList.visibility = state.value["trackList"] ?: View.GONE
        this.binding.trackHistory.visibility = state.value["trackHistory"] ?: View.GONE
        this.binding.stub.visibility = state.value["stub"] ?: View.GONE
        this.binding.stubImage.visibility = state.value["stubImage"] ?: View.GONE
        this.binding.stubPrimaryText.visibility = state.value["stubPrimaryText"] ?: View.GONE
        this.binding.stubSecondaryText.visibility = state.value["stubSecondaryText"] ?: View.GONE
        this.binding.searchScreenStubUpdateButton.visibility =
            state.value["searchScreenStubUpdateButton"] ?: View.GONE
    }

    private fun setContentForNothingFoundState() {
        this.binding.stubPrimaryText.setText(R.string.search_screen_stub_nothing_found_primary_text)
        this.binding.stubSecondaryText.setText(R.string.search_screen_stub_nothing_found_secondary_text)
        this.binding.stubImage.setImageResource(R.drawable.img_nothing_found)
    }

    private fun setContentForConnectionProblemState() {
        this.binding.stubPrimaryText.setText(R.string.search_screen_stub_connection_problem_primary_text)
        this.binding.stubSecondaryText.setText(R.string.search_screen_stub_connection_problem_secondary_text)
        this.binding.stubImage.setImageResource(R.drawable.img_connection_problem)
    }
}