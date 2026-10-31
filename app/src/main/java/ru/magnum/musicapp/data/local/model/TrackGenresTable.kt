package ru.magnum.musicapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "track_genres",
    primaryKeys = ["track_id", "genre_id"],
    foreignKeys = [
        ForeignKey(
            entity = TracksTable::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("track_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GenresTable::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("genre_id"),
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class TrackGenresTable(
    @ColumnInfo("track_id") val trackId: Int,
    @ColumnInfo("genre_id") val genreId: Int
)