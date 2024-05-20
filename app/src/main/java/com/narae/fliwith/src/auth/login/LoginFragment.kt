package com.narae.fliwith.src.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentLoginBinding
import com.narae.fliwith.src.auth.AuthApi
import com.narae.fliwith.src.auth.dto.LoginRequest
import com.narae.fliwith.src.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "싸피"

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    /*
        1. 아이디 비밀번호 입력 후 로그인
        2. 로그인 눌렀을 때 비어 있으면 입력해달라고 표시
        3. 로그인 실패시 에러메시지 표시
        4. 로그인 성공시 토큰 저장후 메인으로 이동
        */
    private fun setListeners() {
        binding.btnLogin.setOnClickListener {
            with(binding) {
                layoutEmail.helperText = null
                layoutPw.helperText = null
                if (etEmail.text.isNullOrEmpty()) {
                    layoutEmail.helperText = "아이디를 입력해주세요."
                    return@setOnClickListener
                }

                if (etPw.text.isNullOrEmpty()) {
                    layoutPw.helperText = "비밀번호를 입력해주세요."
                    return@setOnClickListener
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val loginUser =
                        LoginRequest(binding.etEmail.text.toString(), binding.etPw.text.toString())
                    runCatching {
                        val response = AuthApi.authService.login(loginUser)

                        // 로그인 성공, 200
                        if (response.isSuccessful) {
                            // 유저 토큰정보 저장
                            ApplicationClass.sharedPreferences.addTokenData(response.body()!!.data)
                            startActivity(Intent(context, MainActivity::class.java))
                            requireActivity().finish()
                        }
                        // 로그인 실패, 404
                        else {
                            requireActivity().runOnUiThread {
                                binding.layoutPw.helperText = "아이디 또는 비밀번호를 다시 확인해주세요."
                            }
                        }
                    }.onFailure {
                        Log.d(TAG, "Login: 로그인 실패")
                    }
                }
            }
        }
    }
}