package ru.magnum.musicapp.presentation.сatalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.magnum.musicapp.domain.usecase.GetTracksUseCase
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val getTracksUseCase: GetTracksUseCase
): ViewModel() {
    private val _state = MutableStateFlow(CatalogUIState())
    val state = _state.asStateFlow()

    init {
        getTracks()
    }

    fun getTracks(){
        viewModelScope.launch {
            val tracks = getTracksUseCase()
            _state.update {
                it.copy(
                    tracks = tracks
                )
            }
        }
    }
}