package ru.magnum.musicapp.domain.model

import java.time.LocalDate

data class Album(
    val id: Int,
    val title: String,
    val releaseDate: LocalDate?
)