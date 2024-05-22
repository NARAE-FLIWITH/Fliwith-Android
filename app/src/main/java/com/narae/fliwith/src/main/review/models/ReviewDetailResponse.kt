package com.narae.fliwith.src.main.review.models

import java.io.Serializable

data class ReviewDetailResponse(
    val statusCode: Int,
    val message: String,
    val data: ReviewDetailData
) : Serializable
