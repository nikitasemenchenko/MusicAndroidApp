package ru.magnum.musicapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtistResponse(
    val id: Int,
    val name: String
)
