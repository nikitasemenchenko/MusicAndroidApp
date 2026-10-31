package ru.magnum.musicapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class GenresTable(
    @PrimaryKey val  id: Int,
    val name: String
)