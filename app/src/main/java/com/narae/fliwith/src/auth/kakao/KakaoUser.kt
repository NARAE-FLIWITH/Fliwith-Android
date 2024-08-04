package com.narae.fliwith.src.auth.kakao

import com.narae.fliwith.util.DISABILITY

data class KakaoUser(
    var email: String,
    var nickname: String,
    var disability: DISABILITY?
) {
    constructor() : this("", "", null)
}
