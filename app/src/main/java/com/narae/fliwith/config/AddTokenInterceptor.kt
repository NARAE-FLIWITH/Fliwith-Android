package com.narae.fliwith.config

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.narae.fliwith.config.ApplicationClass.Companion.API_URL
import com.narae.fliwith.config.ApplicationClass.Companion.sharedPreferences
import com.narae.fliwith.src.auth.AuthActivity
import com.narae.fliwith.src.auth.models.TokenData
import com.narae.fliwith.util.showCustomSnackBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

private const val TAG = "싸피"

/**
 * 액세스 토큰 accessBaseTime = 60 * 60 // 60분
 * 리프레쉬 토큰 refreshBaseTime = 60 * 60 * 24 * 7 // 일주일
 * 액세스 토큰 유효시간이 60분이지만 넉넉하게 이전 콜을 보낸지 50분이 넘었으면 자동으로 토큰 재발급 하도록 함
 */
class AddTokenInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val tokenData = sharedPreferences.getTokenData()
        val fiftyMinute = 60 * 50
        val timeDifference = (System.currentTimeMillis() / 1000) - tokenData.createdAt / 1000

        if (timeDifference > fiftyMinute) {
            synchronized(this) {
                val updatedTokenData = sharedPreferences.getTokenData()
                val updatedTimeDifference =
                    (System.currentTimeMillis() / 1000) - updatedTokenData.createdAt / 1000

                if (updatedTimeDifference > fiftyMinute) {
                    if (updatedTokenData.refreshTokenExpirationTime <= System.currentTimeMillis()) {
                        // 리프레쉬 토큰 만료된 경우 재 로그인
                        logout()
                    } else {
                        // 살아있는 리프레시 토큰으로 재발급
                        runBlocking {
                            try {
                                val newTokenData = refreshAccessToken(updatedTokenData.refreshToken)
                                sharedPreferences.setTokenData(newTokenData)
                            } catch (e: ReissueFailException) {
                                //다른 기기로 로그인, 기존 token 변경된 경우
                                Log.e(TAG, "Token reissue failed", e)
                                logout()
                            }
                        }
                    }
                }
            }
        }

        val updatedToken = sharedPreferences.getTokenData().accessToken
        val newRequest = chain.request().newBuilder()
            .header("Authorization", "Bearer $updatedToken")
            .build()

        return chain.proceed(newRequest)
    }

    private fun logout() {
        val intent = Intent(context, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        Log.d(TAG, "intercept: 리프레쉬 토큰 만료")
        sharedPreferences.removeTokenData()
        context.startActivity(intent)
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "토큰이 만료되었습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private suspend fun refreshAccessToken(refreshToken: String): TokenData {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        // 인터셉터 안타게 따로 생성, 이렇게 안해주면 토큰 재발급 요청도 인터셉터를 타게 되어 synchronized 블럭에서 무한대기함
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val response = withContext(Dispatchers.IO) {
            val reissueApi = retrofit.create(ReissueService::class.java)
            reissueApi.reissue(refreshToken)
        }

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.data
        } else {
            throw ReissueFailException()
        }
    }

    class ReissueFailException : IOException() {
        override val message: String
            get() = "Interceptor Refresh failed!!"
    }
}


