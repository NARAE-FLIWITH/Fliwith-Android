package com.narae.fliwith.src.main.recommend.models

import java.io.Serializable

data class TourReview(
    var reviewId: Int,
    var image: String,
    var content:String,
) : Serializable
