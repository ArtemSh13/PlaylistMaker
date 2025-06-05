package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.entities.Track

interface TracksInteractor {
    fun searchTracks(term: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}