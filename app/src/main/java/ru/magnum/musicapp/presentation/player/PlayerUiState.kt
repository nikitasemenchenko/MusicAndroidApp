package ru.magnum.musicapp.presentation.player

import ru.magnum.musicapp.domain.model.Track

data class PlayerUiState(
    val isLoading: Boolean = false,
    val currentTrack: Track? = null
)