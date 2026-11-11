package ru.magnum.musicapp.domain.model

import java.time.LocalDate

data class Track(
    val id: Int,
    val title: String,
    val artists: List<Artist>,
    val albums: List<Album>,
    val genres: List<Genre>,
    val durationMs: Int,
    val releaseDate: LocalDate?,
    val coverUrl: String?,
    val streamUrl: String
)
