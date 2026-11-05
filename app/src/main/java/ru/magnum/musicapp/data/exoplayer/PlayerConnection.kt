package ru.magnum.musicapp.data.exoplayer

import android.content.ComponentName
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.magnum.musicapp.domain.model.Track
import javax.inject.Inject

enum class PlayerConnectionStatus {
    CONNECTING,
    CONNECTED,
    ERROR
}

class PlayerConnection @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val mainHandler = Handler(Looper.getMainLooper())
    private val _connectionStatus = MutableStateFlow(PlayerConnectionStatus.CONNECTING)

    val connectionStatus: StateFlow<PlayerConnectionStatus> = _connectionStatus.asStateFlow()

    private var controller: MediaController? = null
    private var connectionFuture: ListenableFuture<MediaController>? = null
    private var currentTrack: Track? = null

    private var released = false

    init {
        onMain { connect() }
    }

    fun playTrack(track: Track) {
        onMain {
            if (released) return@onMain
            currentTrack = track
            controller?.let { playCurrentTrack(it, track) }
        }
    }

    fun retryConnection() {
        onMain { connect() }
    }

    fun release() {
        onMain {
            if (released) return@onMain

            released = true
            currentTrack = null
            releaseConnection()
        }
    }

    private fun connect() {
        if (released) return //  не пытаемся подключаться после освобождения объекта

        releaseConnection() // сначала освобождаем предыдущее подключение, если оно существовало
        _connectionStatus.value = PlayerConnectionStatus.CONNECTING

        try {
            val token = SessionToken(context, ComponentName(context, MusicService::class.java))
            val future = MediaController.Builder(context, token)
                .setApplicationLooper(Looper.getMainLooper())
                .setListener(
                    object : MediaController.Listener {
                        override fun onDisconnected(controller: MediaController) {
                            onMain {
                                // сравниваем именно ссылки на объекты
                                // проверка для того чтобы отключать правильный контроллер
                                if (this@PlayerConnection.controller === controller) {
                                    failConnection()
                                }
                            }
                        }
                    }
                )
                .buildAsync()

            connectionFuture = future

            // обработчик завершения
            // выполнится после завершения future
            // воспроизведет трек который ждал подключения
            future.addListener(
                {
                    if (released || connectionFuture !== future) return@addListener

                    try {
                        val connectedController = future.get()
                        if (!connectedController.isConnected) {
                            failConnection()
                        } else {
                            controller = connectedController
                            val track = currentTrack
                            if (track == null) {
                                _connectionStatus.value = PlayerConnectionStatus.CONNECTED
                            } else {
                                playCurrentTrack(connectedController, track)
                            }
                        }
                    } catch (_: Exception) {
                        failConnection()
                    }
                },
                ContextCompat.getMainExecutor(context)
            )
        } catch (_: Exception) {
            failConnection()
        }
    }

    private fun playCurrentTrack(controller: MediaController, track: Track) {
        if (released || this.controller !== controller) return

        try {
            check(controller.isConnected)
            check(controller.isCommandAvailable(Player.COMMAND_SET_MEDIA_ITEM))
            check(controller.isCommandAvailable(Player.COMMAND_PREPARE))
            check(controller.isCommandAvailable(Player.COMMAND_PLAY_PAUSE))

            val mediaItem = MediaItem.Builder()
                .setUri(track.streamUrl)
                .setMediaId(track.id.toString())
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(track.title)
                        .setArtist(
                            track.artists.joinToString { it.name }
                        )
                        .build()
                )
                .build()
            controller.setMediaItem(mediaItem)
            controller.prepare()
            controller.play()

            if (released || this.controller !== controller) return
            currentTrack = null
            _connectionStatus.value = PlayerConnectionStatus.CONNECTED
        } catch (_: Exception) {
            failConnection()
        }
    }

    private fun failConnection() {
        if (released) return
        releaseConnection()
        _connectionStatus.value = PlayerConnectionStatus.ERROR
    }

    private fun releaseConnection() {
        controller = null
        connectionFuture?.let { MediaController.releaseFuture(it) }
        connectionFuture = null
    }

    // Обеспечивает выполнение на главном потоке
    private fun onMain(action: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action()
        } else {
            mainHandler.post(action)
        }
    }
}