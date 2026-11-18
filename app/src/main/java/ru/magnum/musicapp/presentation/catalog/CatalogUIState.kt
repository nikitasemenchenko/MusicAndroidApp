package ru.magnum.musicapp.presentation.catalog

import ru.magnum.musicapp.domain.model.Track
import ru.magnum.musicapp.presentation.utils.UiText

data class CatalogUIState(
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = true,
    val isSyncing: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLocalLoaded: Boolean = false,
    val errorMessage: UiText? = null
)