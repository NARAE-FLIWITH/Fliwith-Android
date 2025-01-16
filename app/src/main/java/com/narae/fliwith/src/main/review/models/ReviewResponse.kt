package com.narae.fliwith.src.main.review.models

import java.io.Serializable

data class ReviewResponse(
    val statusCode: Int,
    val message: String,
    val data: ReviewData
) : Serializable


