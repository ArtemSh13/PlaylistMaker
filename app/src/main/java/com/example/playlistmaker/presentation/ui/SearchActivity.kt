package com.example.playlistmaker.presentation.ui

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
import com.example.playlistmaker.Creator
import com.example.playlistmaker.data.network.ITunesApiService
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.R
import com.example.playlistmaker.data.repositories.SharedPreferencesKeeper
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.TracksInteractor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

    private fun showTrackList() {
        this.binding.stub.visibility = View.GONE
        this.binding.searchScreenStubUpdateButton.visibility = View.GONE

        this.binding.trackList.visibility = View.VISIBLE
    }

    private fun clearTrackList() {
        this.binding.trackList.adapter = TrackAdapter(tracks = emptyList(), onTrackClick = { })
    }

    private fun showNothingFoundStub() {
        this.binding.trackList.visibility = View.GONE
        this.binding.searchScreenStubUpdateButton.visibility = View.GONE

        this.binding.stubPrimaryText.setText(R.string.search_screen_stub_nothing_found_primary_text)
        this.binding.stubSecondaryText.setText(R.string.search_screen_stub_nothing_found_secondary_text)
        this.binding.stubImage.setImageResource(R.drawable.img_nothing_found)

        this.binding.stub.visibility = View.VISIBLE
    }

    private fun showConnectionProblemStub() {
        this.binding.trackList.visibility = View.GONE

        this.binding.stubPrimaryText.setText(R.string.search_screen_stub_connection_problem_primary_text)
        this.binding.stubSecondaryText.setText(R.string.search_screen_stub_connection_problem_secondary_text)
        this.binding.stubImage.setImageResource(R.drawable.img_connection_problem)
        this.binding.searchScreenStubUpdateButton.visibility = View.VISIBLE

        this.binding.stub.visibility = View.VISIBLE
    }

    private fun showTracksHistory() {
        val tracksHistory = SharedPreferencesKeeper.getTracksHistory()
        if (tracksHistory.isNotEmpty()) {
            this.binding.trackList.visibility = View.GONE
            this.binding.stub.visibility = View.GONE
            this.binding.trackHistoryTrackList.adapter =
                TrackAdapter(tracks = tracksHistory, onTrackClick = onTrackClickCallback)
            this.binding.trackHistory.visibility = View.VISIBLE
        }
    }

    private fun hideTracksHistory() {
        this.clearTrackList()
        this.binding.trackList.visibility = View.VISIBLE
        this.binding.stub.visibility = View.GONE
        this.binding.trackHistory.visibility = View.GONE
    }

    private val mainHandler = Handler(Looper.getMainLooper())
    private val tracksInteractor = Creator.provideTracksInteractor(this)
    private val searchRunnable = Runnable {
        try {
            binding.progressBar.visibility = View.VISIBLE
            tracksInteractor.searchTracks(
                term = binding.searchBar.text.toString(),
                consumer = object : TracksInteractor.TracksConsumer {
                    override fun consume(foundTracks: List<Track>) {
                        if (foundTracks.isEmpty()) {
                            mainHandler.post {
                                binding.progressBar.visibility = View.GONE
                                showNothingFoundStub()
                            }
                        } else {
                            mainHandler.post {
                                binding.progressBar.visibility = View.GONE
                                binding.trackList.adapter = TrackAdapter(
                                    tracks = foundTracks,
                                    onTrackClick = onTrackClickCallback
                                )
                            }
                        }
                    }
                }
            )
        } catch (e: IllegalStateException) {
            showConnectionProblemStub()
        }
    }

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
            this.showTrackList()
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
            this.searchRunnable
        }

        this.binding.clearTracksHistoryButton.setOnClickListener {
            hideTracksHistory()
            SharedPreferencesKeeper.clearTracksHistory()
        }

        this.binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && this.binding.searchBar.text.isNotEmpty()) {
                this.searchRunnable
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
}