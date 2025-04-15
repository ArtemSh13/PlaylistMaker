package com.example.playlistmaker

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

        val track = intent.getSerializableExtra("clickedTrack") as Track

        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_artwork_placeholder_big)
            .error(R.drawable.ic_artwork_placeholder_big)
            .transform(RoundedCorners(4))
            .into(binding.trackArtwork)

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.trackDescValue.text = track.trackTime
        binding.collectionName.trackDescValue.text = track.collectionName
        binding.releaseDate.trackDescValue.text = track.releaseDate.subSequence(0, 4)
        binding.primaryGenreName.trackDescValue.text = track.primaryGenreName
        binding.country.trackDescValue.text = track.country
    }
}