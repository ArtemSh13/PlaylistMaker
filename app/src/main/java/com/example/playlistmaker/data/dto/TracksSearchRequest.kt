package com.example.playlistmaker.data.dto

data class TracksSearchRequest(
    val term: String,
    val media: String = "music"
)
