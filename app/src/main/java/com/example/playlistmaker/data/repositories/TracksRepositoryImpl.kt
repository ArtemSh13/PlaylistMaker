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
            tracksSearchResponse.results.map {
                Track(
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTime = it.getFormattedTrackTime(),
                    artworkUrl100 = it.artworkUrl100,
                    collectionName = it.getFormattedCollectionName(),
                    releaseDate = it.getFormattedReleaseDate(),
                    primaryGenreName = it.primaryGenreName,
                    country = it.country,
                    previewUrl = it.previewUrl
                )
            }
        } else {
            emptyList()
        }
    }
}