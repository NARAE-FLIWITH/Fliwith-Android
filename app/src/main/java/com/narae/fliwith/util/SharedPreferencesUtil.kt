package com.narae.fliwith.util

import android.content.Context
import android.content.SharedPreferences
import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.config.ApplicationClass.Companion.ACCESS_TOKEN
import com.narae.fliwith.config.ApplicationClass.Companion.CREATED_AT
import com.narae.fliwith.config.ApplicationClass.Companion.GRANT_TYPE
import com.narae.fliwith.config.ApplicationClass.Companion.REFRESH_TOKEN
import com.narae.fliwith.config.ApplicationClass.Companion.REFRESH_TOKEN_EXPIRATION_TIME
import com.narae.fliwith.src.auth.models.TokenData

private const val TAG = "싸피"

class SharedPreferencesUtil(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences(ApplicationClass.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun setTokenData(data: TokenData) {
        val editor = preferences.edit()
        editor.putString(ACCESS_TOKEN, data.accessToken)
        editor.putString(REFRESH_TOKEN, data.refreshToken)
        editor.putString(GRANT_TYPE, data.grantType)
        editor.putLong(REFRESH_TOKEN_EXPIRATION_TIME, data.refreshTokenExpirationTime)
        editor.putLong(CREATED_AT, data.createdAt)
        editor.apply()
    }

    fun getTokenData(): TokenData {
        return TokenData(
            preferences.getString(ACCESS_TOKEN, "")!!,
            preferences.getString(GRANT_TYPE, "")!!,
            preferences.getString(REFRESH_TOKEN, "")!!,
            preferences.getLong(REFRESH_TOKEN_EXPIRATION_TIME, 0),
            preferences.getLong(CREATED_AT, Long.MAX_VALUE)
        )
    }

    fun removeTokenData() {
        val editor = preferences.edit()
        editor.remove(ACCESS_TOKEN)
        editor.remove(GRANT_TYPE)
        editor.remove(REFRESH_TOKEN)
        editor.remove(REFRESH_TOKEN_EXPIRATION_TIME)
        editor.remove(CREATED_AT)
        editor.apply()
    }
}