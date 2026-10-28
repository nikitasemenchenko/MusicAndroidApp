package ru.magnum.musicapp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtistResponse(
    val id: Int,
    val name: String
)
