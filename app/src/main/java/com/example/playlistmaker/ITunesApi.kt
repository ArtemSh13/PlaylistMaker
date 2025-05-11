package com.example.playlistmaker

import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Query

interface ITunesApi {
    @GET("search")
    fun getSongsByTerm(@Query("term") term: String,
                       @Query("media") media: String) : Call<ITunesResponse>
}