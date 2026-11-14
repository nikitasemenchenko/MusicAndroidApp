package ru.magnum.musicapp.presentation.components

import androidx.annotation.StringRes
import ru.magnum.musicapp.R
import ru.magnum.musicapp.data.exoplayer.PlaybackError

@StringRes
fun PlaybackError.toStringRes(): Int {
    return when(this){
        PlaybackError.FILE_NOT_FOUND -> R.string.playback_error_file_not_found
        PlaybackError.NETWORK -> R.string.playback_error_network
        PlaybackError.HTTP -> R.string.playback_error_http
        PlaybackError.TIMEOUT -> R.string.playback_error_timeout
        PlaybackError.UNKNOWN -> R.string.playback_error_unknown
    }
}