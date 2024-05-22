package com.narae.fliwith.src.main.review.models

data class ReviewDetailData(
    val disability: String,
    val nickname: String,
    val createdAt: String,
    val likes: Int,
    val spotName: String,
    val content: String,
    val images: List<String>,
    val mine: Boolean
)
