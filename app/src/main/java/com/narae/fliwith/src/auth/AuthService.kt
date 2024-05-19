package com.narae.fliwith.src.auth

import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.config.dto.ResponseDto
import com.narae.fliwith.src.auth.dto.LoginRequest
import com.narae.fliwith.src.auth.dto.LoginResponse
import com.narae.fliwith.src.auth.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("user/signup/email")
    suspend fun signUp(@Body dto: UserDto): Response<ResponseDto>

    suspend fun isDuplicateEmail()

    suspend fun authMail()

    suspend fun isDuplicateNickname()

    @POST("user/login")
    suspend fun login(@Body dto: LoginRequest): Response<LoginResponse>
}

object AuthApi {
    val authService by lazy {
        ApplicationClass.retrofit.create(AuthService::class.java)
    }
}