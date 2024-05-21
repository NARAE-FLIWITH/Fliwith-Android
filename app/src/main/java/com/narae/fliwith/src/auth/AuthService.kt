package com.narae.fliwith.src.auth

import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.config.dto.ResponseDto
import com.narae.fliwith.src.auth.dto.LoginRequest
import com.narae.fliwith.src.auth.dto.TokenResponse
import com.narae.fliwith.src.auth.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST("user/signup/email")
    suspend fun signUp(@Body dto: UserDto): Response<ResponseDto>


    suspend fun isDuplicateEmail()

    suspend fun authMail()

    suspend fun isDuplicateNickname()

    @POST("user/login")
    suspend fun login(@Body dto: LoginRequest): Response<TokenResponse>

    suspend fun logout()

    @GET("user/reissue")
    suspend fun reissue(@Header("RefreshToken") refreshToken: String): Response<TokenResponse>
}

object AuthApi {
    val authService by lazy {
        ApplicationClass.retrofit.create(AuthService::class.java)
    }
}