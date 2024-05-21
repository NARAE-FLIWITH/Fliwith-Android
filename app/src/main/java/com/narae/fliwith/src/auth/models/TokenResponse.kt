package com.narae.fliwith.src.auth.models

data class TokenResponse(
    val data: TokenData,
    val message: String,
    val statusCode: Int
)