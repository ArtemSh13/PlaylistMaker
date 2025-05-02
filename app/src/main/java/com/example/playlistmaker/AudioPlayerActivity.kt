package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioplayerBinding

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
        this.binding.playButton.isClickable = true
        this.binding.playButton.setOnClickListener { this.playbackControl() }
        this.mediaPlayer.setOnCompletionListener {
            this.binding.playButton.setImageResource(R.drawable.ic_play)
            this.audioPlayerState = STATE_PREPARED
        }
        this.audioPlayerState = STATE_PREPARED
    }

    private fun playAudioPlayer() {
        this.binding.playButton.setImageResource(R.drawable.ic_pause)
        this.mediaPlayer.start()
        this.audioPlayerState = STATE_PLAYING
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
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }
        setContentView(binding.root)

        // Get the track from the previous activity
        val track = intent.getSerializableExtra("clickedTrack") as Track

        // Toolbar
        binding.audioplayerToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back)
        binding.audioplayerToolbar.setNavigationOnClickListener { finish() }

        // Artwork
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_artwork_placeholder_big)
            .error(R.drawable.ic_artwork_placeholder_big)
            .transform(RoundedCorners(4))
            .into(binding.trackArtwork)

        // Main Info
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName

        // Track Description
        binding.trackTime.trackDescKey.setText(R.string.TrackTime_AudioPlayerScreen_PlaylistMaker)
        binding.trackTime.trackDescValue.text = track.trackTime

        binding.collectionName.trackDescKey.setText(R.string.CollectionName_AudioPlayerScreen_PlaylistMaker)
        binding.collectionName.trackDescValue.text = track.collectionName

        binding.releaseDate.trackDescKey.setText(R.string.ReleaseDate_AudioPlayerScreen_PlaylistMaker)
        binding.releaseDate.trackDescValue.text = track.releaseDate

        binding.primaryGenreName.trackDescKey.setText(R.string.PrimaryGenreName_AudioPlayerScreen_PlaylistMaker)
        binding.primaryGenreName.trackDescValue.text = track.primaryGenreName

        binding.country.trackDescKey.setText(R.string.Country_AudioPlayerScreen_PlaylistMaker)
        binding.country.trackDescValue.text = track.country

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