package com.narae.fliwith.src.auth.signup

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentCreateAccountBinding
import com.narae.fliwith.src.auth.AuthApi.authService
import com.narae.fliwith.src.auth.AuthViewModel
import com.narae.fliwith.src.auth.models.EmailRequest
import com.narae.fliwith.util.setOnSingleClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "CreateAccountFragment"

/*
        1. 이메일 입력하면 실시간 형식 체크
        2. 비밀번호 입력하면 형식에 맞는지 실시간 체크(최소 8자, 영문 숫자 포함)
        3. 모두 유효하면 버튼 활성화(실시간)
        4. 다음 눌렀을때 이메일 중복이면 오류 표시
         */
class CreateAccountFragment :
    BaseFragment<FragmentCreateAccountBinding>(FragmentCreateAccountBinding::inflate) {
    private val authViewModel by activityViewModels<AuthViewModel>()
    val pattern = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\S]{8,}$")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                authViewModel.removeEmail()
                authViewModel.removePassword()
                navController.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        binding.user = authViewModel.user
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            authViewModel.removeEmail()
            authViewModel.removePassword()
            navController.popBackStack()
        }

        binding.btnNext.setOnSingleClickListener {
            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    authService.isNotDuplicateEmail(EmailRequest(binding.etEmail.text.toString()))
                }
                if (response.isSuccessful) {
                    authViewModel.setEmail(binding.etEmail.text.toString())
                    authViewModel.setPassword(binding.etPw.text.toString())
                    navController.navigate(R.id.action_createAccountFragment_to_nicknameFragment)
                } else {
                    binding.layoutEmail.error = "이미 가입된 이메일입니다."
                }
            }
        }

        binding.etEmail.addTextChangedListener {
            if (!it.isNullOrEmpty() && !Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                binding.layoutEmail.error = "유효한 이메일 주소를 입력해주세요."
            } else {
                binding.layoutEmail.error = ""
            }
            updateButtonState()
        }

        binding.etPw.addTextChangedListener {
            if (!it.isNullOrEmpty() && !pattern.matches(it)) {
                binding.layoutPw.error = "비밀번호 형식이 맞지 않습니다."
            } else {
                binding.layoutPw.error = ""
            }

            updateButtonState()
        }

        binding.etRepeat.addTextChangedListener {
            if (binding.etPw.text.toString() != it.toString()) {
                binding.layoutRepeat.error = "비밀번호가 일치하지 않습니다."
            } else {
                binding.layoutRepeat.error = ""
            }

            updateButtonState()
        }
    }

    private fun updateButtonState() {
        binding.btnNext.isEnabled = !(binding.etEmail.text.isNullOrBlank()
                or binding.etPw.text.isNullOrBlank()
                or binding.etRepeat.text.isNullOrBlank()
                or !binding.layoutEmail.error.isNullOrEmpty()
                or !binding.layoutPw.error.isNullOrEmpty()
                or !binding.layoutRepeat.error.isNullOrEmpty()
                )
    }
}