package ru.magnum.musicapp.data.repository

import ru.magnum.musicapp.data.mapper.toDomain
import ru.magnum.musicapp.data.remote.api.MusicApi
import ru.magnum.musicapp.domain.model.Track
import ru.magnum.musicapp.domain.repository.TracksRepository
import javax.inject.Inject

class TracksRepositoryImpl @Inject constructor(
    private val musicApi: MusicApi
): TracksRepository {
    override suspend fun getTracks(): List<Track> {
        return musicApi.getTracks().map { it.toDomain() }
    }

}