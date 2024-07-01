package com.narae.fliwith.config

import android.content.Context
import android.content.Intent
import android.util.Log
import com.narae.fliwith.config.ApplicationClass.Companion.sharedPreferences
import com.narae.fliwith.src.auth.AuthActivity
import com.narae.fliwith.src.auth.AuthApi
import com.narae.fliwith.src.auth.models.TokenData
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

private const val TAG = "AddTokenInterceptor"

/**
 * 액세스 토큰 accessBaseTime = 60 * 60 // 60분
 * 리프레쉬 토큰 refreshBaseTime = 60 * 60 * 24 * 7 // 일주일
 */
class AddTokenInterceptor(private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val tokenData = sharedPreferences.getTokenData()
        val fiftyMinute = 60 * 50
        val timeDifference = (System.currentTimeMillis() / 1000) - tokenData.createdAt

        // 토큰이 만료된 경우
        if (timeDifference > fiftyMinute) {
            synchronized(this) {
                val updatedTokenData = sharedPreferences.getTokenData()
                val updatedTimeDifference = (System.currentTimeMillis() / 1000) - updatedTokenData.createdAt

                // 다른 스레드에서 이미 토큰을 재발급했는지 확인
                if (updatedTimeDifference > fiftyMinute) {
                    // 리프레시 토큰 만료 체크
                    if (updatedTokenData.refreshTokenExpirationTime <= System.currentTimeMillis()) {
                        // 사용자를 로그인 액티비티로 이동
                        val intent = Intent(context, AuthActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(intent)
                        return Response.Builder()
                            .request(chain.request())
                            .protocol(Protocol.HTTP_1_1)
                            .code(401) // Unauthorized 상태 코드 사용
                            .message("Unauthorized")
                            .body("".toResponseBody(null))
                            .build()
                    } else {
                        // 살아있는 리프레시 토큰으로 재발급
                        val newTokenData = refreshAccessToken(updatedTokenData.refreshToken)
                        sharedPreferences.setTokenData(newTokenData)
                    }
                }
            }
        }

        // 새로운 액세스 토큰으로 기존 요청을 업데이트
        val updatedToken = sharedPreferences.getTokenData().accessToken
        val newRequest = chain.request().newBuilder()
            .header("Authorization", "Bearer $updatedToken")
            .build()

        // 업데이트된 요청 실행
        return chain.proceed(newRequest)
    }

    private fun refreshAccessToken(refreshToken: String): TokenData {
        val response = runBlocking {
            AuthApi.authService.reissue(refreshToken)
        }

        if(response.isSuccessful && response.body() != null) {
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


