package ru.magnum.musicapp.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.magnum.musicapp.data.exoplayer.PlayerConnection
import ru.magnum.musicapp.domain.repository.TracksRepository
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerConnection: PlayerConnection,
    private val repository: TracksRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState = _uiState.asStateFlow()
    val playbackState = playerConnection.playbackState

    init {
        observePlayback()
    }

    fun togglePlayback(){
        playerConnection.togglePlayback()
    }

    fun seekToNextTrack(){
        playerConnection.seekToNextTrack()
    }

    fun seekToPreviousTrack(){
        playerConnection.seekToPreviousTrack()
    }

    private fun observePlayback() {
        viewModelScope.launch {
            playbackState
                .map { state -> state.currentTrackId }
                .distinctUntilChanged()
                .collectLatest { value ->
                    val id = value?.toIntOrNull()
                    val track = id?.let { repository.getTrack(id) }
                    _uiState.update {
                        it.copy(
                            currentTrack = track
                        )
                    }
                }
        }
    }
}