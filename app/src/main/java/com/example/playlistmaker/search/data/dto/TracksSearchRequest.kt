package com.example.playlistmaker.search.data.dto

data class TracksSearchRequest(
    val term: String,
    val media: String = "music"
)
