package com.example.playlistmaker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object iTunesAPIService {
    val instance = Retrofit.Builder()
    .baseUrl("https://itunes.apple.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build().create<iTunesAPI>()
}