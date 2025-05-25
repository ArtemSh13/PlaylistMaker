package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.entities.Track

interface TracksInteractor {
    fun searchTracks(term: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}