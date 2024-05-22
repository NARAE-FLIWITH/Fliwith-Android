package com.narae.fliwith.config

import android.util.Log
import com.narae.fliwith.config.ApplicationClass.Companion.AUTHORIZATION
import com.narae.fliwith.config.ApplicationClass.Companion.sharedPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

private const val TAG = "AddTokenInterceptor"

// jwt 토큰을 담아주는 인터셉터
class AddTokenInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val grantType = sharedPreferences.getGrantType()
        val accessToken = sharedPreferences.getAccessToken()

        // 일단은 액세스 토큰 담아서 전달
        builder.addHeader(AUTHORIZATION, "$grantType $accessToken")
        Log.d(TAG, "intercept: 토큰 헤더 추가 $grantType $accessToken")
        var response = chain.proceed(builder.build())

        // 액세스 토큰 만료 시 리프레쉬 토큰 재발급
        // 나중에 401 체크로 고쳐야 함
        if (!response.isSuccessful) {
            val expireTime = sharedPreferences.getRefreshTokenExpirationTime()
            val refreshToken = sharedPreferences.getRefreshToken()
            // 리프레쉬 토큰 만료시 -> 재로그인
            if (expireTime <= System.currentTimeMillis()) {
                sharedPreferences.removeTokenData()
                sharedPreferences.setTokenReissueFailed(true)
                Log.d(TAG, "authenticate: 리프레쉬 토큰이 만료되어 재로그인이 필요합니다.")
            }
            // 리프레쉬 토큰 살아있을 때 -> 리프레쉬 토큰 담아서 요청 보내고 재발급
            else {
                response.close()
                val request =
                    chain.request().newBuilder().header(AUTHORIZATION, "Bearer $refreshToken")
                        .build()
                response = chain.proceed(request)
            }
        }
        return response
    }
}