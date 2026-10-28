package ru.magnum.musicapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    val id: Int,
    val name: String
)