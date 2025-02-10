package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.artwork_url_100)

    fun bind(model: Track) {
        this.trackName.text = model.trackName
        this.artistName.text = model.artistName
        this.trackTime.text = model.trackTime
        this.artworkUrl100.tooltipText = model.artworkUrl100
    }
}