package ru.magnum.musicapp.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.magnum.musicapp.data.exoplayer.PlayerConnection
import ru.magnum.musicapp.domain.model.Track
import ru.magnum.musicapp.domain.repository.TracksRepository
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val repository: TracksRepository,
    private val playerConnection: PlayerConnection
) : ViewModel() {
    private val _state = MutableStateFlow(CatalogUIState())
    val state = _state.asStateFlow()
    val playerConnectionStatus = playerConnection.connectionStatus

    private var tracksJob: Job? = null
    private var syncJob: Job? = null

    init {
        getTracks()
    }

    fun getTracks() {
        if (tracksJob?.isActive != true) {
            tracksJob = viewModelScope.launch {
                repository.getTracks()
                    .catch {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Не удалось загрузить каталог"
                            )
                        }
                    }
                    .collect { tracks ->
                        _state.update {
                            it.copy(
                                tracks = tracks,
                                isLoading = tracks.isEmpty() && it.isSyncing,
                                errorMessage = if (tracks.isNotEmpty()) null else it.errorMessage
                            )
                        }
                    }
            }
        }

        if (syncJob?.isActive == true) return
        _state.update {
            it.copy(isLoading = it.tracks.isEmpty(), isSyncing = true, errorMessage = null)
        }
        syncJob = viewModelScope.launch {
            try {
                repository.synchronizeTracks()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        errorMessage = if (it.tracks.isEmpty()) "Не удалось загрузить каталог" else null
                    )
                }
            } finally {
                _state.update { it.copy(isLoading = false, isSyncing = false) }
            }
        }
    }

    fun play(
        tracks: List<Track>,
        index: Int,
        position: Long = 0
        ) {
        playerConnection.playTrack(
            tracks,
            index,
            position
        )
    }

    fun retryPlayerConnection() {
        playerConnection.retryConnection()
    }
}