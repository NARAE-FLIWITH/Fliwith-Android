package com.narae.fliwith.src.main.recommend.models

import com.narae.fliwith.src.main.review.models.Review
import java.io.Serializable

class TourReviewResponse (
    val statusCode: Int,
    val message: String,
    val data: TourReviewData
) : Serializable

