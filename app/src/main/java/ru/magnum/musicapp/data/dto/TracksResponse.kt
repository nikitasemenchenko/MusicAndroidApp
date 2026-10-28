package ru.magnum.musicapp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TracksResponse(
    val id: Int,
    val title: String,
    val artists: List<ArtistResponse>,
    val albums: List<AlbumResponse>,
    val genres: List<GenreResponse>,
    val durationMs: Int,
    val releaseDate: String? = null
)