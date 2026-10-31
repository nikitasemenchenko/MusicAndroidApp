package ru.magnum.musicapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumResponse(
    val id: Int,
    val title: String,
    @SerialName("release_date") val releaseDate: String? = null
)