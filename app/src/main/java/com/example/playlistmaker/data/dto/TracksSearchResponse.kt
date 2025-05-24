package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.entities.Track

class TracksSearchResponse(
    val resultCount: Int,
    val results: ArrayList<TrackDto>
) : Response()