package com.narae.fliwith.src.auth.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val data: TokenData,
    val message: String,
    val statusCode: Int
)