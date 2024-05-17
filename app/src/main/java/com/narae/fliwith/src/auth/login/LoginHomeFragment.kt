package com.narae.fliwith.src.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentLoginBinding
import com.narae.fliwith.databinding.FragmentLoginHomeBinding

class LoginHomeFragment :
    BaseFragment<FragmentLoginHomeBinding>(FragmentLoginHomeBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()

        /*
        1. 이메일 입력하면 실시간 중복 체크
        2. 비밀번호 입력하면 형식에 맞는지 실시간 체크
        3. 모두 유효하면 버튼 활성화(실시간)
        4. 다음 눌렀을때 이메일 중복이면 오류 표시
         */
    }

    private fun setListeners() {
        // 카카오
        binding.btnJoinKakaotalk.setOnClickListener {
            TODO("카카오 소셜 로그인")
        }

        // 회원가입
        binding.btnJoinEmail.setOnClickListener {
            navController.navigate(R.id.action_loginHomeFragment_to_createAccountFragment)
        }

        // 로그인
        binding.btnLogin.setOnClickListener {
            navController.navigate(R.id.action_loginHomeFragment_to_loginFragment)
        }
    }

}