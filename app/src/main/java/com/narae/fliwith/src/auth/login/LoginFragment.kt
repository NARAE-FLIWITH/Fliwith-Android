package com.narae.fliwith.src.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentLoginBinding
import com.narae.fliwith.src.auth.AuthApi
import com.narae.fliwith.src.auth.AuthApi.authService
import com.narae.fliwith.src.auth.models.LoginRequest
import com.narae.fliwith.src.main.LoginViewModel
import com.narae.fliwith.src.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "싸피"

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val loginViewModel by activityViewModels<LoginViewModel>()

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
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.btnLogin.setOnClickListener {
            with(binding) {
                layoutEmail.error = null
                layoutPw.error = null
                if (etEmail.text.isNullOrEmpty()) {
                    layoutEmail.error = "아이디를 입력해주세요."
                    return@setOnClickListener
                }

                if (etPw.text.isNullOrEmpty()) {
                    layoutPw.error = "비밀번호를 입력해주세요."
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    val loginUser =
                        LoginRequest(binding.etEmail.text.toString(), binding.etPw.text.toString())
                    val response = withContext(Dispatchers.IO) { authService.login(loginUser) }

                    // 로그인 성공, 200
                    if (response.isSuccessful) {
                        // 유저 토큰정보 저장
                        loginViewModel.login(response.body()!!.data.apply {
                            createdAt = System.currentTimeMillis()
                        })
                        startActivity(Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }
                    // 로그인 실패, 404
                    else {
                        binding.msgError.visibility = View.VISIBLE
                    }

                }
            }
        }
    }
}