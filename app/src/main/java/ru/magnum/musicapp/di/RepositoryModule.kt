package ru.magnum.musicapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.magnum.musicapp.data.repository.TracksRepositoryImpl
import ru.magnum.musicapp.domain.repository.TracksRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTracksRepository(
        impl: TracksRepositoryImpl
    ): TracksRepository

}