package ru.magnum.musicapp.data.mapper

import ru.magnum.musicapp.BuildConfig
import java.net.URI

private val baseUri = URI.create(BuildConfig.BASE_URL)

fun resolveUrl(url: String): String {
    return baseUri.resolve(url).normalize().toString()
}