package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val trackArtwork: ImageView = itemView.findViewById(R.id.track_artwork)

    fun bind(model: Track) {
        this.trackName.text = model.trackName
        this.artistName.text = model.artistName
        this.trackTime.text = model.trackTime
        Glide.with(this.itemView)
            .load(model.artworkUrl100)
            .transform(RoundedCorners(4))
            .into(trackArtwork)
    }
}