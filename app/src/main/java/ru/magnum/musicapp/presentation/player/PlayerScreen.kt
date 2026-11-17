package ru.magnum.musicapp.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.magnum.musicapp.R
import ru.magnum.musicapp.presentation.components.CoverImage
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

    var isMenuExpanded by remember { mutableStateOf(false) }
    val currentTrackId = playbackState.currentTrackId?.toIntOrNull()

    Scaffold(
        topBar = {
            PlayerTopBar(
                isMenuExpanded = isMenuExpanded,
                currentTrackId = currentTrackId,
                onCollapse = onCollapse,
                onMenuClick = { isMenuExpanded = true },
                onDismissMenu = { isMenuExpanded = false },
                onOpenTrackInfo = onOpenTrackInfo
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 340.dp)
                    .padding(top = 24.dp, bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Обложка
                CoverImage(
                    model = uiState.currentTrack?.coverUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Название и артисты
                TrackHeaderSection(
                    title = uiState.currentTrack?.title.orEmpty(),
                    artists = uiState.currentTrack?.artists?.joinToString { it.name }.orEmpty()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Слайдер времени
                PlaybackProgressSection(
                    positionMs = playbackState.positionMs,
                    durationMs = playbackState.durationMs,
                    onSeekTo = viewModel::seekTo
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Кнопки управления
                PlayerControlsSection(
                    isPlaying = playbackState.playWhenReady && playbackState.error == null,
                    canSeekToPrevious = playbackState.canSeekToPrevious,
                    canSeekToNext = playbackState.canSeekToNext,
                    onTogglePlayback = viewModel::togglePlayback,
                    onPreviousTrack = viewModel::seekToPreviousTrack,
                    onNextTrack = viewModel::seekToNextTrack
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerTopBar(
    isMenuExpanded: Boolean,
    currentTrackId: Int?,
    onCollapse: () -> Unit,
    onMenuClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onOpenTrackInfo: (Int) -> Unit
) {
    TopAppBar(
        title = {},
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
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.more_options)
                    )
                }
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = onDismissMenu
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.track_details)) },
                        enabled = currentTrackId != null,
                        onClick = {
                            onDismissMenu()
                            currentTrackId?.let(onOpenTrackInfo)
                        }
                    )
                }
            }
        }
    )
}

@Composable
private fun TrackHeaderSection(
    title: String,
    artists: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = artists,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaybackProgressSection(
    positionMs: Long,
    durationMs: Long?,
    onSeekTo: (Long) -> Unit
) {
    var isDragging by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    val validDuration = durationMs?.takeIf { it > 0L }
    val displayPosition = (if (isDragging) sliderPosition else positionMs.toFloat())
        .coerceIn(0f, validDuration?.toFloat() ?: 0f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Slider(
            value = displayPosition,
            onValueChange = {
                isDragging = true
                sliderPosition = it
            },
            onValueChangeFinished = {
                validDuration?.let { duration ->
                    onSeekTo(sliderPosition.toLong().coerceIn(0L, duration))
                }
                isDragging = false
            },
            valueRange = 0f..(validDuration?.toFloat() ?: 0f),
            enabled = validDuration != null,
            modifier = Modifier.fillMaxWidth(),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    modifier = Modifier.height(4.dp),
                    thumbTrackGapSize = 0.dp,
                    drawStopIndicator = null
                )
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getDurationString(positionMs),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = getDurationString(durationMs),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PlayerControlsSection(
    isPlaying: Boolean,
    canSeekToPrevious: Boolean,
    canSeekToNext: Boolean,
    onTogglePlayback: () -> Unit,
    onPreviousTrack: () -> Unit,
    onNextTrack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onPreviousTrack,
            enabled = canSeekToPrevious
        ) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = stringResource(R.string.skip_previous_content_description),
                modifier = Modifier.size(36.dp)
            )
        }

        FilledIconButton(
            onClick = onTogglePlayback,
            modifier = Modifier.size(70.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = stringResource(R.string.play_pause_content_description),
                modifier = Modifier.size(40.dp)
            )
        }

        IconButton(
            onClick = onNextTrack,
            enabled = canSeekToNext
        ) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = stringResource(R.string.skip_next_content_description),
                modifier = Modifier.size(36.dp)
            )
        }
    }
}