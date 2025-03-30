package com.example.playlistmaker

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TracksHistoryKeeper {
    private const val TRACKS_HISTORY_SHARED_PREF_FILE_NAME = "playlist_maker_shared_prefs"
    private const val TRACKS_HISTORY_SHARED_PREF_KEY = "tracks_history"
    private const val TRACKS_HISTORY_MAX_LENGTH = 10

    private lateinit var sharedPreferences: SharedPreferences

    fun initSharedPreferencesFromContext(context: Context) {
        this.sharedPreferences = context.getSharedPreferences(
            this.TRACKS_HISTORY_SHARED_PREF_FILE_NAME,
            MODE_PRIVATE
        )
    }

    fun getTracksHistory(): ArrayList<Track> {
        val stringTracksHistory = this.sharedPreferences.getString(
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
        this.sharedPreferences.edit()
            .putString(this.TRACKS_HISTORY_SHARED_PREF_KEY, Gson().toJson(searchHistory))
            .apply()
    }

    fun clearTracksHistory() {
        this.sharedPreferences.edit()
            .putString(this.TRACKS_HISTORY_SHARED_PREF_KEY, Gson().toJson(ArrayList<Track>()))
            .apply()
    }
}