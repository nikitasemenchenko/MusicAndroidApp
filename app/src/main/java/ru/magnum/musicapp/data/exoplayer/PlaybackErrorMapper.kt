package ru.magnum.musicapp.data.exoplayer

import androidx.media3.common.PlaybackException

fun PlaybackException.toPlaybackError(): PlaybackError {
    return when(this.errorCode) {
        PlaybackException.ERROR_CODE_TIMEOUT -> PlaybackError.TIMEOUT
        PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> PlaybackError.NETWORK
        PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> PlaybackError.TIMEOUT
        PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND -> PlaybackError.FILE_NOT_FOUND
        PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS -> PlaybackError.HTTP
        PlaybackException.ERROR_CODE_IO_INVALID_HTTP_CONTENT_TYPE -> PlaybackError.HTTP
        else -> PlaybackError.UNKNOWN
    }
}