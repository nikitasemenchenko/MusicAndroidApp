package ru.magnum.musicapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.magnum.musicapp.data.local.model.AlbumsTable
import ru.magnum.musicapp.data.local.model.ArtistsTable
import ru.magnum.musicapp.data.local.model.GenresTable
import ru.magnum.musicapp.data.local.model.TrackAlbumsTable
import ru.magnum.musicapp.data.local.model.TrackArtistsTable
import ru.magnum.musicapp.data.local.model.TrackGenresTable
import ru.magnum.musicapp.data.local.model.TracksTable

@Database(
    entities = [
        TracksTable::class,
        ArtistsTable::class,
        AlbumsTable::class,
        GenresTable::class,
        TrackArtistsTable::class,
        TrackAlbumsTable::class,
        TrackGenresTable::class
    ],
    version = 1
)
abstract class MusicDatabase: RoomDatabase() {
    abstract fun musicDao(): MusicDao

    companion object {
        const val DB_NAME = "music_db"
    }
}