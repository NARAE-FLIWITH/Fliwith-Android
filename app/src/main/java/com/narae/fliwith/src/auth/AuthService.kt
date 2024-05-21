package com.narae.fliwith.src.auth

import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.config.models.ResponseDto
import com.narae.fliwith.src.auth.models.EmailRequest
import com.narae.fliwith.src.auth.models.LoginRequest
import com.narae.fliwith.src.auth.models.NicknameRequest
import com.narae.fliwith.src.auth.models.TokenResponse
import com.narae.fliwith.src.auth.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("user/signup/email")
    suspend fun signUp(@Body dto: User): Response<ResponseDto>

    @GET("user")
    suspend fun isAuthenticatedEmail(@Query("email") email: String): Response<ResponseDto>

    @POST("user/email")
    suspend fun isNotDuplicateEmail(@Body email: EmailRequest): Response<ResponseDto>

    @POST("user/nickname")
    suspend fun isNotDuplicateNickname(@Body nicknameRequest: NicknameRequest): Response<ResponseDto>

    @POST("user/login")
    suspend fun login(@Body dto: LoginRequest): Response<TokenResponse>

    @GET("user/reissue")
    suspend fun reissue(@Header("RefreshToken") refreshToken: String): Response<TokenResponse>
}

object AuthApi {
    val authService by lazy {
        ApplicationClass.retrofit.create(AuthService::class.java)
    }
}