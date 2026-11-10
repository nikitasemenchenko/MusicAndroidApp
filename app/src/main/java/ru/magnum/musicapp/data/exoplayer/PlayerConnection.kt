package ru.magnum.musicapp.data.exoplayer

import android.content.ComponentName
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.magnum.musicapp.data.mapper.toMediaItem
import ru.magnum.musicapp.domain.model.Track
import javax.inject.Inject
import javax.inject.Singleton

enum class PlayerConnectionStatus {
    CONNECTING,
    CONNECTED,
    ERROR
}

@Singleton
class PlayerConnection @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val mainHandler = Handler(Looper.getMainLooper())
    private val _connectionStatus = MutableStateFlow(PlayerConnectionStatus.CONNECTING)

    val connectionStatus: StateFlow<PlayerConnectionStatus> = _connectionStatus.asStateFlow()

    private var controller: MediaController? = null
    private var connectionFuture: ListenableFuture<MediaController>? = null
    private var pendingPlayback: PendingPlayback? = null


    private val _playbackState = MutableStateFlow(PlaybackState())
    val  playbackState = _playbackState.asStateFlow()

    private var released = false

    init {
        onMain { connect() }
    }

    fun playTrack(
        tracks: List<Track>,
        index: Int,
        positionMs: Long
    ) {
        require(tracks.isNotEmpty()) {"Track list must not be empty"}
        require(index in tracks.indices) { "Index out of bounds" }
        require(positionMs >= 0) {"Position must not be negative"}
        onMain {
            if (released) return@onMain

            val mediaItems = tracks.map { it.toMediaItem() }

            pendingPlayback = PendingPlayback(
                mediaItems,
                index,
                positionMs
            )
            controller?.let {
                play(
                    it,
                    mediaItems,
                    index,
                    positionMs
                )
            }
        }
    }

    fun retryConnection() {
        onMain { connect() }
    }

    fun release() {
        onMain {
            if (released) return@onMain

            released = true
            pendingPlayback = null
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
                        if (connectedController.isConnected) {
                            controller = connectedController


                            // листенер чтобы слушать всякие изменения которые происходят
                            // чтобы на ui обновлялся ползунок проигрывания, переключался трек
                            connectedController.addListener(
                                object : Player.Listener {

                                    override fun onMediaItemTransition(
                                        mediaItem: MediaItem?,
                                        reason: Int
                                    ) {
                                        _playbackState.update {
                                            it.copy(
                                                currentTrackId = mediaItem?.mediaId
                                            )
                                        }
                                        Log.d("PlayerTest", "Playing: ${playbackState.value.currentTrackId}")
                                    }

                                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                                        _playbackState.update {
                                            it.copy(
                                                isPlaying = isPlaying
                                            )
                                        }
                                        Log.d("PlayerTest", "Playing - ${playbackState.value.isPlaying}")
                                    }
                                }
                            )

                            //надо считать начальное состояние потому что
                            //мог быть какой-то трек сохранившийся в плеере до подключения
                            _playbackState.update {
                                it.copy(
                                    currentTrackId = connectedController.currentMediaItem?.mediaId,
                                    isPlaying = connectedController.isPlaying
                                )
                            }

                            val pending = pendingPlayback
                            if (pending == null) {
                                _connectionStatus.value = PlayerConnectionStatus.CONNECTED
                            } else {
                                play(
                                    connectedController,
                                    pending.mediaItems,
                                    pending.currentIndex,
                                    pending.currentPositionMs
                                )
                            }
                        } else {
                            failConnection()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        failConnection()
                    }
                },
                ContextCompat.getMainExecutor(context)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            failConnection()
        }
    }

    private fun play(
        controller: MediaController,
        items: List<MediaItem>,
        trackIndex: Int,
        positionMs: Long
        ) {
        if (released || this.controller !== controller) return

        try {
            check(controller.isConnected)
            check(controller.isCommandAvailable(Player.COMMAND_CHANGE_MEDIA_ITEMS))
            check(controller.isCommandAvailable(Player.COMMAND_PREPARE))
            check(controller.isCommandAvailable(Player.COMMAND_PLAY_PAUSE))

            controller.setMediaItems(
                items,
                trackIndex,
                positionMs
            )
            controller.prepare()
            controller.play()

            if (released || this.controller !== controller) return
            pendingPlayback = null
            _connectionStatus.value = PlayerConnectionStatus.CONNECTED
        } catch (e: Exception) {
            e.printStackTrace()
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