package ru.magnum.musicapp.domain.model

import java.time.LocalDate

data class Album(
    val id: Int,
    val title: Int,
    val releaseDate: LocalDate
)