package com.narae.fliwith.src.main.recommend

import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.src.main.recommend.models.TourRequest
import com.narae.fliwith.src.main.recommend.models.TourResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RecommendService {

    // AI 여행지 추천
    @POST("/tour/ai")
    suspend fun selectAll(@Body request: TourRequest): Response<TourResponse>

}

object RecommendApi {
    val recommendService by lazy {
        ApplicationClass.retrofit.create(RecommendService::class.java)
    }
}