package com.narae.fliwith.src.main.mypage.models

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)