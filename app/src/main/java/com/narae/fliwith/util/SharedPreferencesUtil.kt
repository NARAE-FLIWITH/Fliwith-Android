package com.narae.fliwith.util

import android.content.Context
import android.content.SharedPreferences
import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.src.auth.dto.TokenData

class SharedPreferencesUtil(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences(ApplicationClass.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun addTokenData(data: TokenData) {
        val editor = preferences.edit()
        editor.putString(ApplicationClass.ACCESS_TOKEN, data.accessToken)
        editor.putString(ApplicationClass.REFRESH_TOKEN, data.refreshToken)
        editor.putString(ApplicationClass.GRANT_TYPE, data.grantType)
        editor.putLong(
            ApplicationClass.REFRESH_TOKEN_EXPIRATION_TIME,
            data.refreshTokenExpirationTime
        )
        editor.apply()
    }

    fun getTokenData(): TokenData {
        return TokenData(
            preferences.getString(ApplicationClass.ACCESS_TOKEN, "")!!,
            preferences.getString(ApplicationClass.GRANT_TYPE, "")!!,
            preferences.getString(ApplicationClass.REFRESH_TOKEN, "")!!,
            preferences.getLong(ApplicationClass.REFRESH_TOKEN_EXPIRATION_TIME, 0)
        )
    }

    fun getAccessToken(): String {
        return preferences.getString(ApplicationClass.ACCESS_TOKEN, "")!!
    }

    fun getRefreshToken(): String {
        return preferences.getString(ApplicationClass.REFRESH_TOKEN, "")!!
    }

    fun getGrantType(): String {
        return preferences.getString(ApplicationClass.GRANT_TYPE, "")!!
    }

    fun getRefreshTokenExpirationTime(): Long {
        return preferences.getLong(ApplicationClass.REFRESH_TOKEN_EXPIRATION_TIME, 0)
    }
}