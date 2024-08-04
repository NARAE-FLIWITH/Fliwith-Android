package com.narae.fliwith.src.auth.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentLoginHomeBinding

private const val TAG = "LoginHomeFragment"

class LoginHomeFragment :
    BaseFragment<FragmentLoginHomeBinding>(FragmentLoginHomeBinding::inflate) {

    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "로그인 실패 $error")
        } else if (token != null) {
            Log.e(TAG, "로그인 성공 ${token.accessToken}")
            loggingUserInfo()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners(requireContext())
    }

    private fun setListeners(context: Context) {

        // 카카오 로그인
        binding.btnSignUpKakaotalk.setOnClickListener {
            // 기존에 카카오에 로그인 했던 토큰이 있는지 체크
            if (AuthApiClient.instance.hasToken()) {
                UserApiClient.instance.accessTokenInfo { _, error ->
                    if (error != null) {
                        if (error is KakaoSdkError && error.isInvalidTokenError()) {
                            //로그인 필요
                            kakaoLogin(context)
                        } else {
                            //기타 에러
                            Log.e(TAG, "kakao login error: $error")
                        }
                    } else {
                        //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                        Log.e(TAG, "유효한 카카오 토큰입니다.")
                        loggingUserInfo()
                    }
                }
            } else {
                //로그인 필요
                kakaoLogin(context)
            }
        }

        // 회원가입
        binding.btnSignUpEmail.setOnClickListener {
            navController.navigate(R.id.action_loginHomeFragment_to_createAccountFragment)
        }

        // 일반 로그인
        binding.loginGroup.setOnClickListener {
            navController.navigate(R.id.action_loginHomeFragment_to_loginFragment)
        }
    }

    private fun loggingUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(
                    TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )
            }
        }
    }

    private fun kakaoLogin(context: Context) {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = mCallback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = mCallback)
        }
    }


}