package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.entities.Track

interface TracksRepository {
    fun searchTracks(term: String): List<Track>
}