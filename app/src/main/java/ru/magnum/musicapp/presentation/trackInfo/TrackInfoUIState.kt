package ru.magnum.musicapp.presentation.trackInfo

import ru.magnum.musicapp.domain.model.Track

enum class TrackInfoError {
    NOT_FOUND,
    LOAD_FAILED
}

data class TrackInfoUIState(
    val track: Track? = null,
    val isLoading: Boolean = true,
    val isSyncing: Boolean = false,
    val error: TrackInfoError? = null
)