package ru.magnum.musicapp.data.mapper

import ru.magnum.musicapp.BuildConfig
import java.net.URI

private val baseUri = URI.create(BuildConfig.BASE_URL)

fun resolveUrl(url: String): String? {
    if (url.isBlank()) return null

    return try {
        val resolved = baseUri.resolve(URI.create(url)).normalize()
        if ((resolved.scheme.equals("http", ignoreCase = true) ||
                    resolved.scheme.equals("https", ignoreCase = true)) && resolved.host != null
        ) {
            resolved.toString()
        } else {
            null
        }
    } catch (e: IllegalArgumentException) {
        null
    }
}