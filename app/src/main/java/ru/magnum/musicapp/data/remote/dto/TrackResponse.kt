package ru.magnum.musicapp.data.remote.dto

import kotlinx.serialization.Serializable
import ru.magnum.musicapp.BuildConfig
import ru.magnum.musicapp.domain.model.Track
import java.net.URI

@Serializable
data class TrackResponse(
    val id: Int,
    val title: String,
    val artists: List<ArtistResponse>,
    val albums: List<AlbumResponse>,
    val genres: List<GenreResponse>,
    val durationMs: Int,
    val releaseDate: String? = null,
    val coverUrl: String? = null,
    val streamUrl: String
)