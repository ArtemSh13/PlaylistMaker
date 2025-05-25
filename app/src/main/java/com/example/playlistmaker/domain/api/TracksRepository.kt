package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.util.Resource

interface TracksRepository {
    fun searchTracks(term: String): Resource<List<Track>>
}