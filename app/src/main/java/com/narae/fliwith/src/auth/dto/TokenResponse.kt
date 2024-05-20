package com.narae.fliwith.src.auth.dto

data class TokenResponse(
    val data: TokenData,
    val message: String,
    val statusCode: Int
)