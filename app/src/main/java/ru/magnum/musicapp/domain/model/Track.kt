package ru.magnum.musicapp.domain.model

import java.time.LocalDate

data class Track(
    val id: Int,
    val title: String,
    val artists: List<Artist>,
    val albums: List<Album>,
    val genres: List<Genre>,
    val durationMs: Int,
    val releaseDate: LocalDate?
)

fun Track.getDurationString(): String{
    val totalSec = durationMs / 1000
    val min = totalSec / 60
    val sec = totalSec % 60
    return if(sec < 10) "$min:0$sec" else "$min:$sec"
}
