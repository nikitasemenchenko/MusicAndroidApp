package ru.magnum.musicapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artists")
data class ArtistsTable(
    @PrimaryKey val  id: Int,
    val name: String
)