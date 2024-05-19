package com.narae.fliwith.src.auth.dto

data class TokenData(
    val accessToken: String,
    val grantType: String,
    val refreshToken: String,
    val refreshTokenExpirationTime: Long
)