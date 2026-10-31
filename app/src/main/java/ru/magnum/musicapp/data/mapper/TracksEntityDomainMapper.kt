package ru.magnum.musicapp.data.mapper

import ru.magnum.musicapp.data.local.model.FullTrackInfo
import ru.magnum.musicapp.domain.model.Album
import ru.magnum.musicapp.domain.model.Artist
import ru.magnum.musicapp.domain.model.Genre
import ru.magnum.musicapp.domain.model.Track

fun FullTrackInfo.toDomain(): Track {
    return Track(
        id = this.track.id,
        title = this.track.title,
        artists = this.artists.map {
            Artist(
                id = it.id,
                name = it.name
            )
        },
        albums = this.albums.map { album ->
            Album(
                id = album.id,
                title = album.title,
                releaseDate = parseLocalDate(album.releaseDate)
            )
        },
        genres = this.genres.map {
            Genre(
                id = it.id,
                name = it.name
            )
        },
        durationMs = this.track.durationMs,
        releaseDate = parseLocalDate(this.track.releaseDate),
        coverUrl = this.track.coverUrl?.let { resolveUrl(it) },
        streamUrl = resolveUrl(this.track.streamUrl)
    )
}