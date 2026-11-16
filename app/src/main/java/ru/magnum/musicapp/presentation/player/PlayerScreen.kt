package ru.magnum.musicapp.presentation.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.magnum.musicapp.R
import ru.magnum.musicapp.presentation.utils.getDurationString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    onCollapse: () -> Unit,
    onOpenTrackInfo: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()

    var isDragging by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    val durationMs = playbackState.durationMs?.takeIf { it > 0L }
    val currentTrackId = playbackState.currentTrackId?.toIntOrNull()

    val displayPosition = (
            if (isDragging) sliderPosition
            else playbackState.positionMs.toFloat()
            ).coerceIn(0f, durationMs?.toFloat() ?: 0f)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(uiState.currentTrack?.title ?: stringResource(R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = onCollapse) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.player_collapse)
                        )
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { isMenuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = stringResource(R.string.more_options)
                            )
                        }
                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.track_details)) },
                                enabled = currentTrackId != null,
                                onClick = {
                                    isMenuExpanded = false
                                    currentTrackId?.let(onOpenTrackInfo)
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = uiState.currentTrack?.title ?: "Не загружено"
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Row {
                Button(
                    onClick = viewModel::seekToPreviousTrack,
                    enabled = playbackState.canSeekToPrevious
                ) {
                    Text("Previous")
                }
                Button(
                    onClick = viewModel::togglePlayback
                ) {
                    if (playbackState.isBuffering) {
                        CircularProgressIndicator()
                    } else {
                        Text(
                            if (playbackState.error != null || !playbackState.playWhenReady) {
                                "Play"
                            } else {
                                "Pause"
                            }
                        )
                    }
                }
                Button(
                    onClick = viewModel::seekToNextTrack,
                    enabled = playbackState.canSeekToNext
                ) {
                    Text("Next")
                }
            }
        }
    }
}