package com.narae.fliwith.src.main.review.models

import java.io.Serializable

data class ReviewLikeResponse(
    val statusCode: Int,
    val message: String,
    val data: LikeData
) : Serializable
