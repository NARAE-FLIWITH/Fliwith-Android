package com.narae.fliwith.src.auth

import androidx.lifecycle.ViewModel
import com.narae.fliwith.src.auth.models.User
import com.narae.fliwith.util.DISABILITY

class AuthViewModel : ViewModel() {
    private var _user: User = User()

    val user
        get() = User(
            _user.email,
            _user.password,
            _user.nickname,
            _user.disability
        )

    private var webViewURL: String = ""

    fun removeUser() {
        _user = User()
    }

    fun setWebViewURL(url: String) {
        webViewURL = url
    }

    fun getWebViewURL() = webViewURL

    fun setEmail(email: String) {
        _user.email = email
    }

    fun setPassword(password: String) {
        _user.password = password
    }

    fun setNickname(nickname: String) {
        _user.nickname = nickname
    }

    fun setDisability(disability: DISABILITY?) {
        _user.disability = disability
    }

    fun removeEmail() {
        _user.email = ""
    }

    fun removePassword() {
        _user.password = ""
    }

    fun removeNickname() {
        _user.nickname = ""
    }

    fun removeDisability() {
        _user.disability = null
    }
}