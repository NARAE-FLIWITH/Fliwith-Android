package com.narae.fliwith.config

import com.narae.fliwith.src.auth.models.TokenResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ReissueService {
    @GET("user/reissue")
    suspend fun reissue(@Header("RefreshToken") refreshToken: String): Response<TokenResponse>
}