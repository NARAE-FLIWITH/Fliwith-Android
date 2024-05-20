package com.narae.fliwith.config

import com.narae.fliwith.config.ApplicationClass.Companion.sharedPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

// jwt 토큰을 담아주는 인터셉터
class AddTokenInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val grantType = ApplicationClass.sharedPreferences.getGrantType()
        val jwtToken = sharedPreferences.getAccessToken()
        builder.addHeader("Authorization", "$grantType $jwtToken")
        return chain.proceed(builder.build())
    }
}