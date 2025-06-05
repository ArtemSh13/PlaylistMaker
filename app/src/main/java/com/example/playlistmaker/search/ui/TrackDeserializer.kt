package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.domain.entities.Track
import com.google.gson.*
import java.lang.reflect.Type

class TrackDeserializer : JsonDeserializer<Track> {
    private fun formatMillis(millis: Int): String {
        val seconds = millis / 1000
        val formattedSeconds = if (seconds % 60 < 10) {
            "0" + (seconds % 60).toString()
        } else {
            (seconds % 60).toString()
        }
        val formattedMinutes = (seconds / 60).toString()
        return "$formattedMinutes:$formattedSeconds"
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Track {
        val jsonObject = json.asJsonObject

        return Track(
            trackName = jsonObject.get("trackName").asString,
            artistName = jsonObject.get("artistName").asString,
            trackTime = this.formatMillis(jsonObject.get("trackTimeMillis").asInt),
            artworkUrl100 = jsonObject.get("artworkUrl100").asString,
            collectionName = if (jsonObject.get("collectionName")?.asString.isNullOrEmpty()) {
                ""
            } else {
                jsonObject.get("collectionName").asString
            },
            releaseDate = jsonObject.get("releaseDate").asString.subSequence(0, 4).toString(),
            primaryGenreName = jsonObject.get("primaryGenreName").asString,
            country = jsonObject.get("country").asString,
            previewUrl = jsonObject.get("previewUrl").asString
        )
    }
}