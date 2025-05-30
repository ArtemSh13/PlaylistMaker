package com.example.playlistmaker.presentation.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioplayerBinding

    private val mainHandler = Handler(Looper.getMainLooper())
    private lateinit var trackTimeThread: Thread

    // Create a companion object to control MediaPlayer states
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var audioPlayerState = STATE_DEFAULT

    private val mediaPlayer = MediaPlayer()

    private fun prepareAudioPlayer(track: Track) {
        this.mediaPlayer.setDataSource(track.previewUrl)
        this.mediaPlayer.prepareAsync()
        this.binding.playButton.isEnabled = true
        this.binding.playButton.setOnClickListener { this.playbackControl() }
        this.mediaPlayer.setOnCompletionListener {
            this.binding.playButton.setImageResource(R.drawable.ic_play)
            this.binding.playProgress.setText(R.string.DefaultPlayProgress_AudioPlayerScreen_PlaylistMaker)
            this.audioPlayerState = STATE_PREPARED
        }
        this.audioPlayerState = STATE_PREPARED
    }

    private fun playAudioPlayer() {
        this.binding.playButton.setImageResource(R.drawable.ic_pause)
        this.mediaPlayer.start()
        this.audioPlayerState = STATE_PLAYING
        this.trackTimeThread = Thread {
            while (this.audioPlayerState == STATE_PLAYING) {
                Thread.sleep(250)
                this.mainHandler.post {
                    if (this.audioPlayerState == STATE_PLAYING) {
                        this.binding.playProgress.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    }
                }
            }
        }
        this.trackTimeThread.start()
    }

    private fun pauseAudioPlayer() {
        this.binding.playButton.setImageResource(R.drawable.ic_play)
        this.mediaPlayer.pause()
        this.audioPlayerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when(audioPlayerState) {
            STATE_PREPARED, STATE_PAUSED -> this.playAudioPlayer()
            STATE_PLAYING -> this.pauseAudioPlayer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        this.binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }
        setContentView(this.binding.root)

        // Get the track from the previous activity
        val track = intent.getSerializableExtra("clickedTrack") as Track

        // Toolbar
        this.binding.audioplayerToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back)
        this.binding.audioplayerToolbar.setNavigationOnClickListener { finish() }

        // Artwork
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_artwork_placeholder_big)
            .error(R.drawable.ic_artwork_placeholder_big)
            .transform(RoundedCorners(4))
            .into(this.binding.trackArtwork)

        // Main Info
        this.binding.trackName.text = track.trackName
        this.binding.artistName.text = track.artistName

        // Track Description
        this.binding.trackTime.trackDescKey.setText(R.string.TrackTime_AudioPlayerScreen_PlaylistMaker)
        this.binding.trackTime.trackDescValue.text = track.trackTime

        this.binding.collectionName.trackDescKey.setText(R.string.CollectionName_AudioPlayerScreen_PlaylistMaker)
        this.binding.collectionName.trackDescValue.text = track.collectionName

        this.binding.releaseDate.trackDescKey.setText(R.string.ReleaseDate_AudioPlayerScreen_PlaylistMaker)
        this.binding.releaseDate.trackDescValue.text = track.releaseDate

        this.binding.primaryGenreName.trackDescKey.setText(R.string.PrimaryGenreName_AudioPlayerScreen_PlaylistMaker)
        this.binding.primaryGenreName.trackDescValue.text = track.primaryGenreName

        this.binding.country.trackDescKey.setText(R.string.Country_AudioPlayerScreen_PlaylistMaker)
        this.binding.country.trackDescValue.text = track.country

        // MediaPlayer
        this.prepareAudioPlayer(track)
    }

    override fun onPause() {
        super.onPause()
        this.pauseAudioPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mediaPlayer.release()
    }
}