package com.narae.fliwith.src.auth.models

data class TokenData(
    val accessToken: String,
    val grantType: String,
    val refreshToken: String,
    val refreshTokenExpirationTime: Long,
    var createdAt : Long
)