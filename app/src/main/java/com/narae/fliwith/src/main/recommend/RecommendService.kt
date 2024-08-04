package com.narae.fliwith.src.main.recommend

import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.src.main.recommend.models.TourRequest
import com.narae.fliwith.src.main.recommend.models.TourResponse
import com.narae.fliwith.src.main.recommend.models.TourReviewResponse
import com.narae.fliwith.src.main.review.models.ReviewResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RecommendService {

    // AI 여행지 추천
    @POST("/tour/ai")
    suspend fun selectAll(@Body request: TourRequest): Response<TourResponse>

    // 관광지에 해당하는 리뷰(내용) 목록 조회
    @GET("/tour/review/{contentId}")
    suspend fun selectAllTourReviews(@Path("contentId") contentId: String, @Query("pageNo") pageNo: Int): Response<TourReviewResponse>


}

object RecommendApi {
    val recommendService by lazy {
        ApplicationClass.retrofit.create(RecommendService::class.java)
    }
}