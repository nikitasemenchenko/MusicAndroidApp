package ru.magnum.musicapp.presentation.catalog

import ru.magnum.musicapp.domain.model.Track

data class CatalogUIState(
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = true,
    val isSyncing: Boolean = false,
    val errorMessage: String? = null
)