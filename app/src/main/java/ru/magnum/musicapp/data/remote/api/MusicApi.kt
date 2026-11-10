package ru.magnum.musicapp.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path
import ru.magnum.musicapp.data.remote.dto.TrackResponse

interface MusicApi {
    @GET("api/v1/tracks")
    suspend fun getTracks(): List<TrackResponse>

    @GET("api/v1/tracks/{id}")
    suspend fun getTrack(
        @Path("id") id: Int
    ): TrackResponse
}