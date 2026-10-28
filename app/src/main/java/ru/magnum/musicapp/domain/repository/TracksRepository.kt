package ru.magnum.musicapp.domain.repository

import ru.magnum.musicapp.domain.model.Track

interface TracksRepository {
    suspend fun getTracks():List<Track>
}