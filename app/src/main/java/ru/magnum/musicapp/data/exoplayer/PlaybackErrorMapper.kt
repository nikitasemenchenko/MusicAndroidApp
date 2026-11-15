package ru.magnum.musicapp.data.exoplayer

import androidx.media3.common.PlaybackException
import androidx.media3.datasource.HttpDataSource

fun PlaybackException.toPlaybackError(): PlaybackError {
    return when(this.errorCode) {
        PlaybackException.ERROR_CODE_TIMEOUT -> PlaybackError.TIMEOUT
        PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> PlaybackError.NETWORK
        PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> PlaybackError.TIMEOUT
        PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND -> PlaybackError.FILE_NOT_FOUND
        PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS -> {
            when(httpResponseCode()) {
                404, 410 -> PlaybackError.FILE_NOT_FOUND
                else -> PlaybackError.HTTP
            }
        }
        PlaybackException.ERROR_CODE_IO_INVALID_HTTP_CONTENT_TYPE -> PlaybackError.HTTP
        else -> PlaybackError.UNKNOWN
    }
}

//функция извлекает HTTP код ответа
//создает последовательность исключений, начиная с самой ошибки и спускается по цепочке ее причин
//фильтрует по классу который хранит статус ответа сервера
//нужна из-за того что сетевые ошибки оборачиваются в более общие исключения
private fun Throwable.httpResponseCode(): Int? {
    return generateSequence(this) { it.cause }
        .filterIsInstance<HttpDataSource.InvalidResponseCodeException>().firstOrNull()?.responseCode
}