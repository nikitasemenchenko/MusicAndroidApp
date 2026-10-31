package ru.magnum.musicapp.data.mapper

import ru.magnum.musicapp.data.local.model.AlbumsTable
import ru.magnum.musicapp.data.local.model.ArtistsTable
import ru.magnum.musicapp.data.local.model.GenresTable
import ru.magnum.musicapp.data.local.model.TracksTable
import ru.magnum.musicapp.data.remote.dto.AlbumResponse
import ru.magnum.musicapp.data.remote.dto.ArtistResponse
import ru.magnum.musicapp.data.remote.dto.GenreResponse
import ru.magnum.musicapp.data.remote.dto.TrackResponse

//URL храню те же что и отдает API, вдруг потом baseURL поменяется, в базе ничего не надо будет менять
fun TrackResponse.toEntity(): TracksTable {
    return TracksTable(
        id = this.id,
        title = this.title,
        durationMs = this.durationMs,
        releaseDate = this.releaseDate,
        coverUrl = this.coverUrl,
        streamUrl = this.streamUrl
    )
}

fun ArtistResponse.toEntity(): ArtistsTable {
    return ArtistsTable(
        id = this.id,
        name = this.name
    )
}

fun AlbumResponse.toEntity(): AlbumsTable {
    return AlbumsTable(
        id = this.id,
        title = this.title,
        releaseDate = this.releaseDate
    )
}

fun GenreResponse.toEntity(): GenresTable {
    return GenresTable(
        id = this.id,
        name = this.name
    )
}