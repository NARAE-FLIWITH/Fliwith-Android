package com.narae.fliwith.src.main.review.models

import java.io.Serializable

data class Review(
    var reviewId: Int,
    var image: String,
    var disability:String,
    var nickname:String,
    var likes:Int
) : Serializable