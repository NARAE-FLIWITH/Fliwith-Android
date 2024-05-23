package com.narae.fliwith.src.main.review.models

data class ReviewInsertRequest(
    val contentId: Int,
    val content: String,
    val images: List<String>
)