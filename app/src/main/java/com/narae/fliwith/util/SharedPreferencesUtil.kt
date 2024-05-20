package com.narae.fliwith.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.config.ApplicationClass.Companion.IS_VALID_TOKEN
import com.narae.fliwith.config.ApplicationClass.Companion.REFRESH_TOKEN_EXPIRATION_TIME
import com.narae.fliwith.src.auth.dto.TokenData

private const val TAG = "싸피"

class SharedPreferencesUtil(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences(ApplicationClass.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun addTokenData(data: TokenData) {
        val editor = preferences.edit()
        editor.putString(ApplicationClass.ACCESS_TOKEN, data.accessToken)
        editor.putString(ApplicationClass.REFRESH_TOKEN, data.refreshToken)
        editor.putString(ApplicationClass.GRANT_TYPE, data.grantType)
        editor.putLong(
            REFRESH_TOKEN_EXPIRATION_TIME,
            data.refreshTokenExpirationTime
        )
        editor.apply()
    }

    fun getTokenData(): TokenData {
        return TokenData(
            preferences.getString(ApplicationClass.ACCESS_TOKEN, "")!!,
            preferences.getString(ApplicationClass.GRANT_TYPE, "")!!,
            preferences.getString(ApplicationClass.REFRESH_TOKEN, "")!!,
            preferences.getLong(REFRESH_TOKEN_EXPIRATION_TIME, 0)
        )
    }

    fun removeTokenData() {
        Log.d(TAG, "removeTokenData")
        val editor = preferences.edit()
        editor.remove(ApplicationClass.ACCESS_TOKEN)
        editor.remove(ApplicationClass.GRANT_TYPE)
        editor.remove(ApplicationClass.REFRESH_TOKEN)
        editor.remove(REFRESH_TOKEN_EXPIRATION_TIME)
        editor.apply()
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
        return preferences.getLong(REFRESH_TOKEN_EXPIRATION_TIME, 0)
    }

    fun setTokenReissueFailed(flag: Boolean) {
        preferences.edit().putBoolean(IS_VALID_TOKEN, flag).apply()
    }

    fun getTokenReissueFailed(): Boolean {
        return preferences.getBoolean(IS_VALID_TOKEN, false)
    }
}