package ru.magnum.musicapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.magnum.musicapp.data.exoplayer.PlayerConnection
import ru.magnum.musicapp.domain.player.MusicPlayer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlayerModule {

    @Binds
    @Singleton
    abstract fun bindMusicPlayer(
        impl: PlayerConnection
    ): MusicPlayer
}