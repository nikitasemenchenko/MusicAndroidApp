package ru.magnum.musicapp.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.magnum.musicapp.domain.model.Track

interface TracksRepository {
    suspend fun synchronizeTracks()
    fun getTracks(): Flow<List<Track>>
}