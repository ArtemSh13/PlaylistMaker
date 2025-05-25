package com.example.playlistmaker.data.repositories

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.util.Resource

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(term: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(term))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Check your internet connection")
            }
            200 -> {
                Resource.Success((response as TracksSearchResponse).results.map {
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
                })
            }

            else -> {
                Resource.Error("Server error")
            }
        }
    }
}