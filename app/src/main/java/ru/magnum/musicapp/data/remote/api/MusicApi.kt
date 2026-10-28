package ru.magnum.musicapp.data.remote.api

import retrofit2.http.GET
import ru.magnum.musicapp.data.dto.TracksResponse

interface MusicApi {
    @GET("api/v1/tracks")
    suspend fun getTracks(): TracksResponse
}