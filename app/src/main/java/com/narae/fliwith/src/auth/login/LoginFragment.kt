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

        /*
        1. 아이디 비밀번호 입력 후 로그인
        2. 이메일이 유효하지 않으면 에러 표시
        3. 비밀번호가 유효하지 않으면 에러 표시
        4. 모두 유효하다면 화면 이동
         */
    }

    private fun setListeners(){
    }

    private fun checkInput(){
    }
}