package com.example.playlistmaker

import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TracksHistoryKeeper {
    private const val TRACKS_HISTORY_SHARED_PREF_FILE_NAME = "playlist_maker_shared_prefs"
    private const val TRACKS_HISTORY_SHARED_PREF_KEY = "tracks_history"
    private const val TRACKS_HISTORY_MAX_LENGTH = 10

    lateinit var activity: AppCompatActivity

    fun getTracksHistory(): ArrayList<Track> {
        val sharedPreferences = this.activity.getSharedPreferences(
            this.TRACKS_HISTORY_SHARED_PREF_FILE_NAME,
            MODE_PRIVATE
        )
        val stringTracksHistory = sharedPreferences.getString(
            this.TRACKS_HISTORY_SHARED_PREF_KEY, ""
        )

        return if (stringTracksHistory.isNullOrEmpty()) {
            ArrayList()
        } else {
            Gson().fromJson(
                stringTracksHistory,
                object : TypeToken<ArrayList<Track>>() {}.type
            )
        }
    }

    fun recordTrackInHistory(track: Track) {
        val searchHistory = this.getTracksHistory()
        searchHistory.remove(track)
        searchHistory.add(0, track)
        if (searchHistory.size > this.TRACKS_HISTORY_MAX_LENGTH) {
            searchHistory.removeAt(searchHistory.size - 1)
        }
        val sharedPreferences = activity.getSharedPreferences(
            this.TRACKS_HISTORY_SHARED_PREF_FILE_NAME,
            MODE_PRIVATE
        )
        sharedPreferences.edit()
            .putString(this.TRACKS_HISTORY_SHARED_PREF_KEY, Gson().toJson(searchHistory))
            .apply()
    }
}