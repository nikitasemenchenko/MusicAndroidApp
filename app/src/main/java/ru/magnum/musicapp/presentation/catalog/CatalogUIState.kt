package ru.magnum.musicapp.presentation.catalog

import ru.magnum.musicapp.domain.model.Track

data class CatalogUIState(
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = false,    // Первичная загрузка
    val isSyncing: Boolean = false, // Фоновое обновление с сервера
    val errorMessage: String? = null
)