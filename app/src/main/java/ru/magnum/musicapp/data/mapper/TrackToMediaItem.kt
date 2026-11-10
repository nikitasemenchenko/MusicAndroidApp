package ru.magnum.musicapp.data.mapper

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import ru.magnum.musicapp.domain.model.Track

fun Track.toMediaItem(): MediaItem = MediaItem.Builder().apply {
    setUri(streamUrl)
    setMediaId(id.toString())
    setMediaMetadata(
        MediaMetadata.Builder().apply {
            setTitle(title)
            setArtist(artists.joinToString { it.name })
        }.build()
    )
}.build()