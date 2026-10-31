package ru.magnum.musicapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.magnum.musicapp.data.local.model.AlbumsTable
import ru.magnum.musicapp.data.local.model.ArtistsTable
import ru.magnum.musicapp.data.local.model.FullTrackInfo
import ru.magnum.musicapp.data.local.model.GenresTable
import ru.magnum.musicapp.data.local.model.TrackAlbumsTable
import ru.magnum.musicapp.data.local.model.TrackArtistsTable
import ru.magnum.musicapp.data.local.model.TrackGenresTable
import ru.magnum.musicapp.data.local.model.TracksTable

@Dao
interface MusicDao {
    @Transaction
    @Query("SELECT * FROM tracks")
    fun getTracks(): Flow<List<FullTrackInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<TracksTable>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtists(artists: List<ArtistsTable>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbums(albums: List<AlbumsTable>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGenres(genres: List<GenresTable>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackArtists(links: List<TrackArtistsTable>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackAlbums(links: List<TrackAlbumsTable>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackGenres(links: List<TrackGenresTable>)

    @Query("DELETE FROM track_artists")
    suspend fun clearTrackArtists()

    @Query("DELETE FROM track_albums")
    suspend fun clearTrackAlbums()

    @Query("DELETE FROM track_genres")
    suspend fun clearTrackGenres()

    @Query("DELETE FROM tracks")
    suspend fun clearTracks()

    @Query("DELETE FROM artists")
    suspend fun clearArtists()

    @Query("DELETE FROM albums")
    suspend fun clearAlbums()

    @Query("DELETE FROM genres")
    suspend fun clearGenres()
}