package ru.magnum.musicapp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AlbumResponse(
    val id: Int,
    val title: String,
    val releaseDate: String? = null
)