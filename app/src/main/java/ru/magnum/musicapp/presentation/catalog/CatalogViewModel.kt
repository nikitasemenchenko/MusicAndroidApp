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
import ru.magnum.musicapp.domain.player.MusicPlayer
import ru.magnum.musicapp.domain.repository.TracksRepository
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val repository: TracksRepository,
    private val musicPlayer: MusicPlayer
) : ViewModel() {
    private val _state = MutableStateFlow(CatalogUIState())
    val state = _state.asStateFlow()
    val playbackState = musicPlayer.playbackState

    private var tracksJob: Job? = null
    private var syncJob: Job? = null

    init {
        getTracks()
    }

    fun getTracks(isPullToRefresh: Boolean = false) {
        if (tracksJob?.isActive != true) {
            tracksJob = viewModelScope.launch {
                repository.getTracks()
                    .catch {
                        _state.update {
                            it.copy(
                                isLocalLoaded = true,
                                isLoading = false
                            )
                        }
                    }
                    .collect { tracks ->
                        _state.update {
                            it.copy(
                                tracks = tracks,
                                isLocalLoaded = true,
                                isLoading = false,
                                errorMessage = if (tracks.isNotEmpty()) null else it.errorMessage
                            )
                        }
                    }
            }
        }

        if (syncJob?.isActive == true) return
        _state.update {
            it.copy(
                isSyncing = true,
                isRefreshing = isPullToRefresh,
                errorMessage = null
            )
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
                _state.update {
                    it.copy(
                        isSyncing = false,
                        isRefreshing = false
                    )
                }
            }
        }
    }

    fun onTrackClick(index: Int) {
        val tracks = _state.value.tracks
        val track = tracks.getOrNull(index) ?: return

        if (playbackState.value.currentTrackId == track.id.toString()) {
            musicPlayer.togglePlayback()
        } else {
            musicPlayer.playTrack(tracks, index, 0L)
        }
    }

    fun retryPlayerConnection() {
        musicPlayer.retryConnection()
    }
}