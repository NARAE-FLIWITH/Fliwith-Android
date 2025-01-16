package com.narae.fliwith.src.main.map.models

import com.google.gson.annotations.SerializedName

data class SpotListResponse(
    @SerializedName("data") val spotList: List<SpotWithLocation>,
    val message: String,
    val statusCode: Int
)