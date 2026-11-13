package ru.magnum.musicapp.data.exoplayer

data class PlaybackState(
    val currentTrackId: String? = null,
    val isPlaying: Boolean = false, //аудио реально воспроизводится
    val isBuffering: Boolean = false, //плеер загружает аудио
    val playWhenReady: Boolean = false, // пользователь запросил воспроизведение
    val positionMs: Long = 0L,
    val durationMs: Long? = null,
    val canSeekToNext: Boolean = false,
    val canSeekToPrevious: Boolean = false,
    val errorMessage: String? = null
)
