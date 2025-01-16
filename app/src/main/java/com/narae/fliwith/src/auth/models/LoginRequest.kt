package com.narae.fliwith.src.auth.models

data class LoginRequest(
    val email: String,
    val password: String
)