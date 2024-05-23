package com.narae.fliwith.src.main.mypage

import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.config.models.ResponseDto
import com.narae.fliwith.src.main.mypage.models.ProfileResponse
import com.narae.fliwith.src.main.review.models.ReviewResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MyPageService {

    @POST("user/logout")
    suspend fun logout(): Response<ResponseDto>

    @GET("user/profile")
    suspend fun getProfile(): Response<ProfileResponse>

    @GET("review/list/like")
    suspend fun getLikeReviews(@Query("pageNo") pageNo: Int): Response<ReviewResponse>

    @GET("review/list/write")
    suspend fun getWriteReviews(@Query("pageNo") pageNo: Int): Response<ReviewResponse>
}

object MyPageApi {
    val myPageService by lazy {
        ApplicationClass.retrofit.create(MyPageService::class.java)
    }
}