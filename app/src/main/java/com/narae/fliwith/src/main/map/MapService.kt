package com.narae.fliwith.src.main.map

import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.src.main.map.models.SpotListResponse
import com.narae.fliwith.src.main.recommend.models.TourResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MapService {

    @GET("tour")
    suspend fun searchByLocation(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): Response<SpotListResponse>

    @GET("tour/{contentTypeId}/{contentId}")
    suspend fun getSpotDetail(
        @Path("contentTypeId") contentTypeId : String,
        @Path("contentId") contentId : String
    ) : Response<TourResponse>
}

object MapApi {
    val mapService by lazy {
        ApplicationClass.retrofit.create(MapService::class.java)
    }
}