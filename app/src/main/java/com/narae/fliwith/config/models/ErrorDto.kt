package com.narae.fliwith.config.models

data class ErrorDto(
    val errorCode: String,
    val message: String,
    val timeStamp: String
)