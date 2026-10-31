package ru.magnum.musicapp.data.repository

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.magnum.musicapp.data.local.MusicDao
import ru.magnum.musicapp.data.local.MusicDatabase
import ru.magnum.musicapp.data.local.model.TrackAlbumsTable
import ru.magnum.musicapp.data.local.model.TrackArtistsTable
import ru.magnum.musicapp.data.local.model.TrackGenresTable
import ru.magnum.musicapp.data.mapper.toDomain
import ru.magnum.musicapp.data.mapper.toEntity
import ru.magnum.musicapp.data.remote.api.MusicApi
import ru.magnum.musicapp.data.remote.dto.TrackResponse
import ru.magnum.musicapp.domain.model.Track
import ru.magnum.musicapp.domain.repository.TracksRepository
import javax.inject.Inject

class TracksRepositoryImpl @Inject constructor(
    private val musicApi: MusicApi,
    private val database: MusicDatabase,
    private val musicDao: MusicDao,
): TracksRepository {

    override suspend fun synchronizeTracks() {
        val tracksDto = musicApi.getTracks()
        saveTracks(tracksDto)
    }

    override fun getTracks(): Flow<List<Track>> {
        return musicDao.getTracks().map { list -> list.map { it.toDomain() } }
    }

    private suspend fun saveTracks(tracksDto: List<TrackResponse>) {
        if (tracksDto.isEmpty()) return

        val tracksTables = tracksDto.map { it.toEntity() }
        val artistsTables = tracksDto.flatMap { track -> track.artists.map { it.toEntity() } }.distinctBy { it.id }
        val albumsTables = tracksDto.flatMap { track -> track.albums.map { it.toEntity() } }.distinctBy { it.id }
        val genresTables = tracksDto.flatMap { track -> track.genres.map { it.toEntity() } }.distinctBy { it.id }

        val trackArtistsTables = tracksDto.flatMap { track ->
            track.artists.map { artist ->
                TrackArtistsTable(trackId = track.id, artistId = artist.id)
            }
        }.distinct()

        val trackAlbumsTables = tracksDto.flatMap { track ->
            track.albums.map { album ->
                TrackAlbumsTable(trackId = track.id, albumId = album.id)
            }
        }.distinct()

        val trackGenresTables = tracksDto.flatMap { track ->
            track.genres.map { genre ->
                TrackGenresTable(trackId = track.id, genreId = genre.id)
            }
        }.distinct()

        database.withTransaction {
            musicDao.clearTrackArtists()
            musicDao.clearTrackAlbums()
            musicDao.clearTrackGenres()
            musicDao.clearTracks()
            musicDao.clearArtists()
            musicDao.clearAlbums()
            musicDao.clearGenres()

            if (artistsTables.isNotEmpty()) musicDao.insertArtists(artistsTables)
            if (albumsTables.isNotEmpty()) musicDao.insertAlbums(albumsTables)
            if (genresTables.isNotEmpty()) musicDao.insertGenres(genresTables)

            if (tracksTables.isNotEmpty()) musicDao.insertTracks(tracksTables)

            if (trackArtistsTables.isNotEmpty()) musicDao.insertTrackArtists(trackArtistsTables)
            if (trackAlbumsTables.isNotEmpty()) musicDao.insertTrackAlbums(trackAlbumsTables)
            if (trackGenresTables.isNotEmpty()) musicDao.insertTrackGenres(trackGenresTables)
        }
    }

}