package com.narae.fliwith.src.auth.kakao

import androidx.lifecycle.ViewModel
import com.narae.fliwith.util.DISABILITY

class KakaoAuthViewModel : ViewModel() {
    private var _kakaoUser = KakaoUser()

    val kakaoUser
        get() = KakaoUser(
            _kakaoUser.email,
            _kakaoUser.nickname,
            _kakaoUser.disability
        )

    private var webViewURL: String = ""

    fun setWebViewURL(url: String) {
        webViewURL = url
    }

    fun getWebViewURL() = webViewURL

    fun removeKakaoUser() {
        _kakaoUser = KakaoUser()
    }

    fun setEmail(email: String) {
        _kakaoUser.email = email
    }

    fun setNickname(nickname: String) {
        _kakaoUser.nickname = nickname
    }

    fun setDisability(disability: DISABILITY?) {
        _kakaoUser.disability = disability
    }

    fun removeEmail() {
        _kakaoUser.email = ""
    }

    fun removeNickname() {
        _kakaoUser.nickname = ""
    }

    fun removeDisability() {
        _kakaoUser.disability = null
    }
}