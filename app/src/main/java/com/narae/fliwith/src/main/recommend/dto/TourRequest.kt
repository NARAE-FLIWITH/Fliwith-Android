package com.narae.fliwith.src.main.recommend.dto

import com.narae.fliwith.src.main.DISABILITY

data class TourRequest(
    val area: String,
    val contentType: String,
    val disability: DISABILITY,
    val peopleNum: Int,
    val visitedDate: String
)
