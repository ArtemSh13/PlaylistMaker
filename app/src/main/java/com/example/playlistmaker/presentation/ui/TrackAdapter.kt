package com.example.playlistmaker.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.repositories.SharedPreferencesKeeper
import com.example.playlistmaker.domain.entities.Track

class TrackAdapter(private val tracks: List<Track>,
                   private val onTrackClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            SharedPreferencesKeeper.recordTrackInHistory(tracks[position])
            onTrackClick(tracks[position])
        }
    }

    override fun getItemCount() = tracks.size
}