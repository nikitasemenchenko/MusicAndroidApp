package ru.magnum.musicapp.domain.player

import kotlinx.coroutines.flow.StateFlow
import ru.magnum.musicapp.domain.model.PlaybackState
import ru.magnum.musicapp.domain.model.PlayerConnectionStatus
import ru.magnum.musicapp.domain.model.Track


interface MusicPlayer {
    val connectionStatus: StateFlow<PlayerConnectionStatus>
    val playbackState: StateFlow<PlaybackState>

    fun playTrack(
        tracks: List<Track>,
        index: Int,
        positionMs: Long = 0L
    )

    fun togglePlayback()

    fun seekToNextTrack()

    fun seekToPreviousTrack()

    fun seekTo(position: Long)

    fun retryConnection()
}