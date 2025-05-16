package com.example.playlistmaker.data.network

import com.example.playlistmaker.domain.entities.Track

class ITunesResponse(
    val resultCount: Int,
    val results: ArrayList<Track>
) {
}