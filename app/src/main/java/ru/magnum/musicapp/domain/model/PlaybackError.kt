package ru.magnum.musicapp.domain.model

enum class PlaybackError {
    FILE_NOT_FOUND,
    NETWORK,
    HTTP,
    TIMEOUT,
    UNKNOWN
}