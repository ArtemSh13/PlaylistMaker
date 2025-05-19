package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Query

interface ITunesApi {
    @GET("search")
    fun getSongsByTerm(@Query("term") term: String,
                       @Query("media") media: String) : Call<TracksSearchResponse>
}