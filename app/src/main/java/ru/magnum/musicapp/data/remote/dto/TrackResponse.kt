package ru.magnum.musicapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackResponse(
    val id: Int,
    val title: String,
    val artists: List<ArtistResponse>,
    val albums: List<AlbumResponse>,
    val genres: List<GenreResponse>,
    @SerialName("duration_ms") val durationMs: Int,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("cover_url") val coverUrl: String? = null,
    @SerialName("stream_url") val streamUrl: String
)