package ru.magnum.musicapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "track_albums",
    primaryKeys = ["track_id", "album_id"],
    foreignKeys = [
        ForeignKey(
            entity = TracksTable::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("track_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AlbumsTable::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("album_id"),
            onDelete = ForeignKey.RESTRICT
        )
    ]
    )
data class TrackAlbumsTable(
    @ColumnInfo("track_id") val trackId: Int,
    @ColumnInfo("album_id") val albumId: Int
)