package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.entities.Track
import com.example.playlistmaker.util.Resource

interface TracksRepository {
    fun searchTracks(term: String): Resource<List<Track>>
}