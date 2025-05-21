package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.entities.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(term: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(term))
        check(response.resultCode == 200)
        val tracksSearchResponse = response as TracksSearchResponse
        return if (tracksSearchResponse.resultCount != 0) {
            tracksSearchResponse.results
        } else {
            emptyList()
        }
    }
}