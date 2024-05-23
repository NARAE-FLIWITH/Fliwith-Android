package com.narae.fliwith.src.main.review.models

import java.io.Serializable

data class ReviewSpotNameResponse(
    val statusCode: Int,
    val message: String,
    val data: List<ReviewSpotName>
) : Serializable
