package com.narae.fliwith.src.auth.login

import android.os.Bundle
import android.view.View
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentLoginHomeBinding

class LoginHomeFragment :
    BaseFragment<FragmentLoginHomeBinding>(FragmentLoginHomeBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        // 카카오
        binding.btnSignUpKakaotalk.setOnClickListener {
            TODO("카카오 소셜 로그인")
        }

        // 회원가입
        binding.btnSignUpEmail.setOnClickListener {
            navController.navigate(R.id.action_loginHomeFragment_to_createAccountFragment)
        }

        // 로그인
        binding.loginGroup.setOnClickListener {
            navController.navigate(R.id.action_loginHomeFragment_to_loginFragment)
        }


    }

}