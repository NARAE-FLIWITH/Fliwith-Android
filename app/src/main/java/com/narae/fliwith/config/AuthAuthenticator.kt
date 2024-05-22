package com.narae.fliwith.config

import android.util.Log
import com.narae.fliwith.config.ApplicationClass.Companion.AUTHORIZATION
import com.narae.fliwith.config.ApplicationClass.Companion.sharedPreferences
import com.narae.fliwith.src.auth.AuthApi
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
        runBlocking {
            Log.d(TAG, "authenticate: 엥?")
            refreshToken = sharedPreferences.getRefreshToken()
            reissueResponse = AuthApi.authService.reissue(refreshToken)
        }
        // 리프레쉬 토큰 살아 있음 -> 액세스 토큰 재발급
        if (reissueResponse.code() == 200) {
            Log.d(TAG, "authenticate: 액세스 토큰 재발급 성공")
            val responseData = reissueResponse.body() as TokenResponse
            sharedPreferences.addTokenData(responseData.data)
            return newRequestWithToken(responseData.data.accessToken, response.request)
        }
        // 리프레쉬 토큰 만료 -> 로그인 화면 보내기
        else {
            sharedPreferences.setTokenReissueFailed(true)
            Log.d(TAG, "authenticate: 리프레쉬 토큰이 만료되어 재로그인이 필요합니다.")
        }
        return null
    }

    private fun newRequestWithToken(token: String, request: Request): Request {
        return request.newBuilder()
            .header(AUTHORIZATION, "Bearer $token")
            .build()
    }
}