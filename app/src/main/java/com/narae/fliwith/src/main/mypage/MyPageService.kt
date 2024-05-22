package com.narae.fliwith.src.main.mypage

import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.config.models.ResponseDto
import com.narae.fliwith.src.main.mypage.models.ProfileResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface MyPageService {

    @POST("user/logout")
    suspend fun logout(): Response<ResponseDto>

    @GET("user/profile")
    suspend fun getProfile() : Response<ProfileResponse>
}

object MyPageApi {
    val myPageService by lazy {
        ApplicationClass.retrofit.create(MyPageService::class.java)
    }
}