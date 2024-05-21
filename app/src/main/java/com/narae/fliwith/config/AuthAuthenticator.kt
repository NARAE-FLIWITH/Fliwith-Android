package com.narae.fliwith.config

import android.util.Log
import com.narae.fliwith.config.ApplicationClass.Companion.AUTHORIZATION
import com.narae.fliwith.config.ApplicationClass.Companion.sharedPreferences
import com.narae.fliwith.src.auth.AuthApi.authService
import com.narae.fliwith.src.auth.models.TokenResponse
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Route
import okhttp3.Response
import retrofit2.Response as RetrofitResponse

private const val TAG = "AuthAuthenticator"

// 액세스 토큰이 만료되었는지를 체크하는 authenticator
class AuthAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        var refreshToken: String
        var reissueResponse: RetrofitResponse<TokenResponse>
        val expireTime = sharedPreferences.getRefreshTokenExpirationTime()

        // 리프레쉬 토큰이 만료되었다면 재로그인
        if (expireTime <= System.currentTimeMillis()) {
            sharedPreferences.removeTokenData()
            sharedPreferences.setTokenReissueFailed(true)
            Log.d(TAG, "authenticate: 리프레쉬 토큰이 만료되어 재로그인이 필요합니다.")
            return null
        } else {
            runBlocking {
                refreshToken = sharedPreferences.getRefreshToken()
                reissueResponse = authService.reissue(refreshToken)
            }
            // 리프레쉬 토큰 살아 있음 -> 액세스 토큰 재발급
            if (reissueResponse.isSuccessful) {
                Log.d(TAG, "authenticate: 액세스 토큰 재발급 성공")
                val responseData = reissueResponse.body() as TokenResponse
                sharedPreferences.addTokenData(responseData.data)
                return newRequestWithToken(responseData.data.accessToken, response.request)
            } else {
                Log.d(TAG, "authenticate: 액세스 토큰 재발급 실패")
            }
        }
        return null
    }

    private fun newRequestWithToken(token: String, request: Request): Request {
        return request.newBuilder()
            .header(AUTHORIZATION, "Bearer $token")
            .build()
    }
}