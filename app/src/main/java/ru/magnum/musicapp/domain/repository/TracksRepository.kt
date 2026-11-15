package ru.magnum.musicapp.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.magnum.musicapp.domain.model.Track

interface TracksRepository {
    suspend fun synchronizeTracks()
    fun getTracks(): Flow<List<Track>>
    suspend fun getTrack(id: Int): Track?
    fun observeTrack(id: Int): Flow<Track?>
    suspend fun refreshTrack(id: Int)
}