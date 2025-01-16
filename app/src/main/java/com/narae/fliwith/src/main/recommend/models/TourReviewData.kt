package com.narae.fliwith.src.main.recommend.models


data class TourReviewData(
    val reviews: List<TourReview>,
    val pageNo: Int,
    val lastPageNo: Int
)
