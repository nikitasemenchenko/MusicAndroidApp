package ru.magnum.musicapp.data.mapper

fun getDurationString(durationMs: Long?): String{
    if(durationMs == null || durationMs <0) return "--:--"
    val totalSec = durationMs / 1000
    val min = totalSec / 60
    val sec = totalSec % 60
    return "%02d:%02d".format(min, sec)
}