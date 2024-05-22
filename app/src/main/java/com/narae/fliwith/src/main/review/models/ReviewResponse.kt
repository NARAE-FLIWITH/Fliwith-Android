package com.narae.fliwith.src.main.review.models

import java.io.Serializable

data class ReviewResponse(
    val statusCode: Int,
    val message: String,
    val data: ReviewData
) : Serializable

data class ReviewData(
    val reviews: List<Review>,
    val pageNo: Int,
    val lastPageNo: Int
)
