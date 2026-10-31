package ru.magnum.musicapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TracksTable(
    @PrimaryKey val id: Int,
    val title: String,
    @ColumnInfo("duration_ms") val durationMs: Int,
    @ColumnInfo("release_date") val releaseDate: String? = null,
    @ColumnInfo("cover_url") val coverUrl: String? = null,
    @ColumnInfo("stream_url") val streamUrl: String
)