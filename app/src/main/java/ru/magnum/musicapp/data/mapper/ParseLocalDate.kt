package ru.magnum.musicapp.data.mapper

import java.time.LocalDate

fun parseLocalDate(date: String?): LocalDate? {
    if (date.isNullOrBlank()) return null
    return runCatching { LocalDate.parse(date) }.getOrNull()
}