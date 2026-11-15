package ru.magnum.musicapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
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

    @Transaction
    @Query("SELECT * FROM tracks WHERE id = :id")
    suspend fun getTrack(id: Int): FullTrackInfo?

    @Transaction
    @Query("SELECT * FROM tracks WHERE id = :id")
    fun observeTrack(id: Int): Flow<FullTrackInfo?>

    //Upsert = INSERT, а при конфликте по ключу UPDATE. Строка не удаляется, поэтому CASCADE и RESTRICT не срабатывают
    @Upsert
    suspend fun upsertTracks(tracks: List<TracksTable>)

    @Upsert
    suspend fun upsertArtists(artists: List<ArtistsTable>)

    @Upsert
    suspend fun upsertAlbums(albums: List<AlbumsTable>)

    @Upsert
    suspend fun upsertGenres(genres: List<GenresTable>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackArtists(links: List<TrackArtistsTable>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackAlbums(links: List<TrackAlbumsTable>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackGenres(links: List<TrackGenresTable>)

    @Query("DELETE FROM track_artists WHERE track_id = :trackId")
    suspend fun deleteTrackArtists(trackId: Int)

    @Query("DELETE FROM track_albums WHERE track_id = :trackId")
    suspend fun deleteTrackAlbums(trackId: Int)

    @Query("DELETE FROM track_genres WHERE track_id = :trackId")
    suspend fun deleteTrackGenres(trackId: Int)

    //Связи удалённых треков удалит CASCADE
    @Query("DELETE FROM tracks WHERE id NOT IN (:ids)")
    suspend fun deleteTracksNotIn(ids: List<Int>)

    @Query("DELETE FROM tracks WHERE id = :id")
    suspend fun deleteTrack(id: Int)

    //Удаляем только записи, на которые больше никто не ссылается, поэтому RESTRICT не мешает
    @Query("DELETE FROM artists WHERE id NOT IN (SELECT artist_id FROM track_artists)")
    suspend fun deleteOrphanArtists()

    @Query("DELETE FROM albums WHERE id NOT IN (SELECT album_id FROM track_albums)")
    suspend fun deleteOrphanAlbums()

    @Query("DELETE FROM genres WHERE id NOT IN (SELECT genre_id FROM track_genres)")
    suspend fun deleteOrphanGenres()
}
