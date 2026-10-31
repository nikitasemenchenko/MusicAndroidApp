package ru.magnum.musicapp.data.local.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class FullTrackInfo(
    @Embedded val track: TracksTable,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = TrackArtistsTable::class,
            parentColumn = "track_id",
            entityColumn = "artist_id"
        )
    )
    val artists: List<ArtistsTable>,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = TrackAlbumsTable::class,
            parentColumn = "track_id",
            entityColumn = "album_id"
        )
    )
    val albums: List<AlbumsTable>,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = TrackGenresTable::class,
            parentColumn = "track_id",
            entityColumn = "genre_id"
        )
    )
    val genres: List<GenresTable>
)
