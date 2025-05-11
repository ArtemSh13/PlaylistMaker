package com.example.playlistmaker

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ITunesApiService {
    val gson = GsonBuilder()
        .registerTypeAdapter(Track::class.java, TrackDeserializer())
        .create()

    val instance = Retrofit.Builder()
    .baseUrl("https://itunes.apple.com/")
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build().create<ITunesApi>()
}