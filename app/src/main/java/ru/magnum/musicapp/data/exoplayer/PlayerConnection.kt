package ru.magnum.musicapp.data.exoplayer

import android.content.ComponentName
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.magnum.musicapp.domain.model.Track
import javax.inject.Inject

class PlayerConnection @Inject constructor(
    @ApplicationContext context: Context
) {
    //false - подключение к медиа сессии не установлено
    //true - media controller подключился
    private val _controllerState = MutableStateFlow(false)
    val controllerState = _controllerState.asStateFlow()

    private val sessionToken = SessionToken(
        context,
        ComponentName(context, MusicService::class.java)
    )
    private val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

    private var mediaController: MediaController? = null

    init {
        controllerFuture.addListener(
            {
                try {
                    mediaController = controllerFuture.get()
                    _controllerState.value = true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            ContextCompat.getMainExecutor(context)
        )
    }

    fun playTrack(track: Track) {
        if(controllerState.value){
            val mediaItem = MediaItem.Builder()
                .setUri(track.streamUrl)
                .setMediaId(track.id.toString())
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(track.title)
                        .build()
                )
                .build()
            mediaController?.setMediaItem(mediaItem)
            mediaController?.prepare()
            mediaController?.play()
        }
    }

    fun release() {
        _controllerState.value = false
        MediaController.releaseFuture(controllerFuture)
    }
}