package ru.magnum.musicapp.presentation.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()

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
        Text(
            text = if(playbackState.isPlaying) "Воспроизведение" else "Пауза"
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
                Text(
                    if (playbackState.isPlaying) "Pause" else "Play"
                )
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