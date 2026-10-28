package ru.magnum.musicapp.presentation.сatalog

import ru.magnum.musicapp.domain.model.Track

data class CatalogUIState(
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)