package com.narae.fliwith.src.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.narae.fliwith.config.ApplicationClass.Companion.sharedPreferences
import com.narae.fliwith.src.auth.models.TokenData

class LoginViewModel : ViewModel() {
    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus : LiveData<Boolean> get() = _loginStatus

    fun login(tokenData : TokenData){
        sharedPreferences.setTokenData(tokenData)
        _loginStatus.value = true
    }

    fun logout(){
        sharedPreferences.removeTokenData()
        _loginStatus.value = false
    }
}