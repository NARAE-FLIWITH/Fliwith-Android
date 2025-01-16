package com.narae.fliwith.src.auth.models

import com.narae.fliwith.util.DISABILITY

data class KakaoSignUpRequest(
    val kakaoId: Long,
    val nickname: String,
    val disability: DISABILITY
)