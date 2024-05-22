package com.narae.fliwith.src.main.recommend.dto
import com.narae.fliwith.util.DISABILITY
import java.io.Serializable

data class TourRequest(
    val area: String,
    val contentType: String,
    val disability: DISABILITY,
    val peopleNum: Int,
    val visitedDate: String
) : Serializable
