package com.narae.fliwith.src.main.mypage

import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.config.dto.ResponseDto
import com.narae.fliwith.src.auth.AuthService
import retrofit2.Response
import retrofit2.http.POST

interface MyPageService {

    @POST("user/logout")
    suspend fun logout(): Response<ResponseDto>
}

object MyPageApi {
    val myPageService by lazy {
        ApplicationClass.retrofit.create(MyPageService::class.java)
    }
}