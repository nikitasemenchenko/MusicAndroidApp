package ru.magnum.musicapp.data.mapper

import ru.magnum.musicapp.data.remote.dto.AlbumResponse
import ru.magnum.musicapp.data.remote.dto.ArtistResponse
import ru.magnum.musicapp.data.remote.dto.GenreResponse
import ru.magnum.musicapp.data.remote.dto.TrackResponse
import ru.magnum.musicapp.domain.model.Album
import ru.magnum.musicapp.domain.model.Artist
import ru.magnum.musicapp.domain.model.Genre
import ru.magnum.musicapp.domain.model.Track

fun ArtistResponse.toDomain(): Artist {
    return Artist(
        id = this.id,
        name = this.name
    )
}

fun GenreResponse.toDomain(): Genre {
    return Genre(
        id = this.id,
        name = this.name
    )
}

fun AlbumResponse.toDomain(): Album {
    return Album(
        id = this.id,
        title = this.title,
        releaseDate = parseLocalDate(this.releaseDate)
    )
}

fun TrackResponse.toDomain(): Track {
    return Track(
        id = this.id,
        title = this.title,
        artists = this.artists.map { it.toDomain() },
        albums = this.albums.map { it.toDomain() },
        genres = this.genres.map { it.toDomain() },
        durationMs = this.durationMs,
        releaseDate = parseLocalDate(this.releaseDate),
        coverUrl = this.coverUrl?.let { resolveUrl(it) },
        streamUrl = resolveUrl(this.streamUrl)
    )
}