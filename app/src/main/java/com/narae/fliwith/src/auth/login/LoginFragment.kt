package com.narae.fliwith.src.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        /*
        1. 아이디 비밀번호 입력 후 로그인
        1-1 로그인 눌렀을 때 비어 있으면 입력해달라고 표시
        2. 이메일이 유효하지 않으면 에러 표시
        3. 비밀번호가 유효하지 않으면 에러 표시
        4. 모두 유효하다면 화면 이동
         */
    }

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

                /*
                로그인 요청
                성공 -> 메인으로 넘어가기
                실패 -> 에러 메시지 보여주기
                 */
                // 로그인 요청

                // 성공
                // 실패

            }
        }
    }

    private fun checkInput() {
    }
}