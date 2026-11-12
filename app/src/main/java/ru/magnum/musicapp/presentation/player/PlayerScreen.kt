package ru.magnum.musicapp.presentation.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.magnum.musicapp.data.mapper.getDurationString

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()

    var isDragging by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val durationMs = playbackState.durationMs?.takeIf { it > 0L }

    val displayPosition = (
            if (isDragging) sliderPosition
            else playbackState.positionMs.toFloat()
            ).coerceIn(0f, durationMs?.toFloat() ?: 0f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Text(
            text = uiState.currentTrack?.title ?: "Не загружено"
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            Text(getDurationString(playbackState.positionMs))
            Text(getDurationString(playbackState.durationMs))
        }

        Slider(
            value = displayPosition,
            onValueChange = {
                isDragging = true
                sliderPosition = it
            },
            onValueChangeFinished = {
                durationMs?.let { duration ->
                    viewModel.seekTo(
                        sliderPosition.toLong().coerceIn(0L, duration)
                    )
                }
                isDragging = false
            },
            valueRange = 0f..(durationMs?.toFloat() ?: 0f),
            enabled = durationMs != null,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Row {
            Button(
                onClick = viewModel::seekToPreviousTrack,
                enabled = playbackState.canSeekToPrevious
            ) {
                Text(
                    "Previous"
                )
            }
            Button(
                onClick = viewModel::togglePlayback
            ) {
                if(playbackState.isBuffering){
                    CircularProgressIndicator()
                } else {
                    Text(
                        if (playbackState.playWhenReady) "Pause" else "Play"
                    )
                }
            }
            Button(
                onClick = viewModel::seekToNextTrack,
                enabled = playbackState.canSeekToNext
            ) {
                Text(
                    "Next"
                )
            }
        }
    }

}