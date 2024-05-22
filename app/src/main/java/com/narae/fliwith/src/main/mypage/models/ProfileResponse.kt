package com.narae.fliwith.src.main.mypage.models

data class ProfileResponse(
    val data: Profile,
    val message: String,
    val statusCode: Int
)