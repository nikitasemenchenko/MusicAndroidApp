package ru.magnum.musicapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class AlbumsTable(
    @PrimaryKey val  id: Int,
    val title: String,
    @ColumnInfo("release_date") val releaseDate: String? = null
)