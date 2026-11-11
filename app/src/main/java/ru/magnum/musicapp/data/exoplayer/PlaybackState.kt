package ru.magnum.musicapp.data.exoplayer

data class PlaybackState(
    val currentTrackId: String? = null,
    val isPlaying: Boolean = false,
    val canSeekToNext: Boolean = false,
    val canSeekToPrevious: Boolean = false
)
