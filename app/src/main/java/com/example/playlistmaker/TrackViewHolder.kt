package com.example.playlistmaker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackTextDescription: TextView = itemView.findViewById(R.id.trackTextDescription)

    fun bind(model: Track) {
        trackTextDescription.text = model.trackName
    }
}