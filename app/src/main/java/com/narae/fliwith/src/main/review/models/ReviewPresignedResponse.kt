package com.narae.fliwith.src.main.review.models

import java.io.Serializable

data class ReviewPresignedResponse(
    val statusCode: Int,
    val message: String,
    val data: PresignedData
) : Serializable
