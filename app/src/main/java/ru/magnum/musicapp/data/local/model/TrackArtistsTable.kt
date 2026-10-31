package ru.magnum.musicapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "track_artists",
    primaryKeys = ["track_id", "artist_id"],
    foreignKeys = [
        ForeignKey(
            entity = TracksTable::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("track_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ArtistsTable::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("artist_id"),
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class TrackArtistsTable(
    @ColumnInfo("track_id") val trackId: Int,
    @ColumnInfo("artist_id") val artistId: Int
)