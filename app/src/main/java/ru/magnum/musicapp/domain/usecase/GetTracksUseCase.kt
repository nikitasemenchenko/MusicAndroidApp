package ru.magnum.musicapp.domain.usecase

import ru.magnum.musicapp.domain.model.Track
import ru.magnum.musicapp.domain.repository.TracksRepository
import javax.inject.Inject

class GetTracksUseCase @Inject constructor(
    private val tracksRepository: TracksRepository
) {
    suspend operator fun invoke(): List<Track> {
        return tracksRepository.getTracks()
    }
}