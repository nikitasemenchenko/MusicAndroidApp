package ru.magnum.musicapp.data.exoplayer

import androidx.media3.common.MediaItem

data class PendingPlayback(
    val mediaItems: List<MediaItem>,
    val currentIndex: Int,
    val currentPositionMs: Long
)
