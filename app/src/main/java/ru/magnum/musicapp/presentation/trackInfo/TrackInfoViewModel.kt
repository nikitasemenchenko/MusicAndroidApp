package ru.magnum.musicapp.presentation.trackInfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.magnum.musicapp.domain.repository.TrackNotFoundException
import ru.magnum.musicapp.domain.repository.TracksRepository
import ru.magnum.musicapp.presentation.navigation.TrackInfoScreen
import javax.inject.Inject

@HiltViewModel
class TrackInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: TracksRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TrackInfoUIState())
    val uiState = _uiState.asStateFlow()

    private val trackId: Int = savedStateHandle.toRoute<TrackInfoScreen>().id

    private var trackJob: Job? = null
    private var syncJob: Job? = null

    init {
        observeTrack()
        refreshTrack()
    }

    private fun observeTrack() {
        if (trackJob?.isActive != true) {
            trackJob = viewModelScope.launch {
                repository.observeTrack(trackId)
                    .catch { exception ->
                        if (exception is CancellationException) throw exception
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = if (it.track == null) TrackInfoError.LOAD_FAILED else null
                            )
                        }
                    }
                    .collect { track ->
                        _uiState.update {
                            it.copy(
                                track = track,
                                isLoading = track == null && it.isSyncing && it.error != TrackInfoError.NOT_FOUND,
                                error = if (track != null) null else it.error
                            )
                        }
                    }
            }
        }
    }

    fun refreshTrack() {
        if (syncJob?.isActive == true) return
        _uiState.update {
            it.copy(
                isLoading = it.track == null,
                isSyncing = true,
                error = null
            )
        }
        syncJob = viewModelScope.launch {
            try {
                repository.refreshTrack(trackId)
            } catch (e: CancellationException) {
                throw e
            } catch (e: TrackNotFoundException) {
                _uiState.update {
                    it.copy(
                        track = null,
                        isLoading = false,
                        error = TrackInfoError.NOT_FOUND
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = if (it.track == null) TrackInfoError.LOAD_FAILED else null
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSyncing = false
                    )
                }
            }
        }
    }
}