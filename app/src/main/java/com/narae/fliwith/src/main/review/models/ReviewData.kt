package com.narae.fliwith.src.main.review.models

data class ReviewData(
    val reviews: List<Review>,
    val pageNo: Int,
    val lastPageNo: Int
)
