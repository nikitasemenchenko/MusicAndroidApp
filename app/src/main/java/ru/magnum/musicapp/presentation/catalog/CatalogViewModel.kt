package ru.magnum.musicapp.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.magnum.musicapp.domain.repository.TracksRepository
import javax.inject.Inject

import kotlinx.coroutines.flow.catch

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val repository: TracksRepository
): ViewModel() {
    private val _state = MutableStateFlow(CatalogUIState())
    val state = _state.asStateFlow()

    init {
        getTracks()
    }

    fun getTracks() {
        // Если треков в кеше еще нет, то показываем крутилку
        if (_state.value.tracks.isEmpty()) {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
        } else {
            // Если треки уже есть, включаем фоновый индикатор обновления
            _state.update { it.copy(isSyncing = true, errorMessage = null) }
        }

        // Показываем кеш
        viewModelScope.launch {
            repository.getTracks()
                .catch { e ->
                    _state.update { it.copy(isLoading = false, isSyncing = false, errorMessage = e.message) }
                }
                .collect { tracks ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            tracks = tracks
                        )
                    }
                }
        }

        // Синхронизируем с сервером в фоне
        viewModelScope.launch {
            runCatching {
                repository.synchronizeTracks()
            }.onFailure { e ->
                // Ошибку сети показываем только если у нас вообще нет кэша
                _state.update {
                    it.copy(
                        isSyncing = false,
                        errorMessage = if (it.tracks.isEmpty()) e.message else null
                    )
                }
            }.onSuccess {
                _state.update { it.copy(isSyncing = false) }
            }
        }
    }
}