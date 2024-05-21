package com.narae.fliwith.src.auth.models

import com.narae.fliwith.util.DISABILITY

data class User(
    var email: String,
    var password: String,
    var nickname: String,
    var disability: DISABILITY?
) {
    constructor() : this("", "", "", null)
}