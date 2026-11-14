package ru.magnum.musicapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.magnum.musicapp.R
import ru.magnum.musicapp.data.exoplayer.PlaybackState
import ru.magnum.musicapp.domain.model.Track

@Composable
fun MiniPlayer(
    track: Track,
    onOpenPlayer: () -> Unit,
    onTogglePlayback: () -> Unit,
    onSkipToPrevious: () -> Unit,
    onSkipToNext: () -> Unit,
    playbackState: PlaybackState
) {
    val progress = playbackState.durationMs
        ?.takeIf { it > 0L }
        ?.let { duration ->
            (playbackState.positionMs.toFloat() / duration)
                .coerceIn(0f, 1f)
        } ?: 0f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onOpenPlayer()
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoverImage(
            model = track.coverUrl,
            modifier = Modifier.size(60.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = track.title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = track.artists.joinToString { it.name },
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(
                    onClick = {
                        onSkipToPrevious()
                    },
                    enabled = playbackState.canSeekToPrevious
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = stringResource(R.string.skip_previous_content_description)
                    )
                }
                IconButton(
                    onClick = {
                        onTogglePlayback()
                    },
                ) {
                    Icon(
                        imageVector = if (playbackState.playWhenReady) {
                            Icons.Default.Pause
                        } else {
                            Icons.Default.PlayArrow
                        },
                        contentDescription = stringResource(R.string.play_pause_content_description)
                    )
                }
                IconButton(
                    onClick = {
                        onSkipToNext()
                    },
                    enabled = playbackState.canSeekToNext
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = stringResource(R.string.skip_next_content_description)
                    )
                }
            }
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}