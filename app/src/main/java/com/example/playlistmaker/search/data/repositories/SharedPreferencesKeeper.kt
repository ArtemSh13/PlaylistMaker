package com.example.playlistmaker.search.data.repositories

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.entities.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferencesKeeper {
    private const val SHARED_PREF_FILE_NAME = "playlist_maker_shared_prefs"
    private const val DARK_THEME_SWITCH_KEY = "dark_theme_switch"
    private const val TRACKS_HISTORY_SHARED_PREF_KEY = "tracks_history"
    private const val TRACKS_HISTORY_MAX_LENGTH = 10

    private lateinit var sharedPreferences: SharedPreferences

    fun initSharedPreferencesFromContext(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            SHARED_PREF_FILE_NAME,
            MODE_PRIVATE
        )
    }

    fun getDarkThemeSwitchState(): Boolean {
        return sharedPreferences.getBoolean(
            DARK_THEME_SWITCH_KEY, false
        )
    }

    fun setDarkThemeSwitchState(state: Boolean) {
        sharedPreferences.edit()
            .putBoolean(DARK_THEME_SWITCH_KEY, state)
            .apply()
    }

    fun getTracksHistory(): ArrayList<Track> {
        val stringTracksHistory = sharedPreferences.getString(
            TRACKS_HISTORY_SHARED_PREF_KEY, ""
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
        val searchHistory = getTracksHistory()
        searchHistory.remove(track)
        searchHistory.add(0, track)
        if (searchHistory.size > TRACKS_HISTORY_MAX_LENGTH) {
            searchHistory.removeAt(searchHistory.size - 1)
        }
        sharedPreferences.edit()
            .putString(TRACKS_HISTORY_SHARED_PREF_KEY, Gson().toJson(searchHistory))
            .apply()
    }

    fun clearTracksHistory() {
        sharedPreferences.edit()
            .putString(TRACKS_HISTORY_SHARED_PREF_KEY, Gson().toJson(ArrayList<Track>()))
            .apply()
    }
}