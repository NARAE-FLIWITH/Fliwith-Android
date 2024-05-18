package com.narae.fliwith.src.auth

import com.narae.fliwith.config.dto.ResponseDto
import com.narae.fliwith.src.auth.dto.userDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("user/signup/email")
    suspend fun signUp(@Body dto : userDto) : Response<ResponseDto>

    suspend fun isDuplicateEmail()

    suspend fun authMail()

    suspend fun isDuplicateNickname()

    suspend fun login()

}