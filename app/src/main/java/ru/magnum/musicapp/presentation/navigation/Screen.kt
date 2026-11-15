package ru.magnum.musicapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object CatalogScreen

@Serializable
object PlayerScreen

@Serializable
data class TrackInfoScreen(
    val id: Int
)