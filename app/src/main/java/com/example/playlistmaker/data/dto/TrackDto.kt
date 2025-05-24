package com.example.playlistmaker.data.dto

data class TrackDto(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Int, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String?, // Название альбома
    val releaseDate: String, // Год релиза трека
    val primaryGenreName: String, // Жанр трека
    val country: String, // Страна исполнителя
    val previewUrl: String // Ссылка на аудиофайл
) {
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

    fun getFormattedTrackTime(): String = this.formatMillis(this.trackTimeMillis)

    fun getFormattedCollectionName(): String = this.collectionName ?: ""

    fun getFormattedReleaseDate(): String = this.releaseDate.subSequence(0, 4).toString()
}