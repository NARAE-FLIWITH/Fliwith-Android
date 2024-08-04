package com.narae.fliwith.src.auth.kakao

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentKakaoNicknameBinding
import com.narae.fliwith.src.auth.AuthApi
import com.narae.fliwith.src.auth.models.NicknameRequest
import com.narae.fliwith.util.setOnSingleClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KakaoNicknameFragment : BaseFragment<FragmentKakaoNicknameBinding>(
    FragmentKakaoNicknameBinding::inflate
) {

    private val kakaoAuthViewModel by activityViewModels<KakaoAuthViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                kakaoAuthViewModel.removeNickname()
                navController.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        binding.user = kakaoAuthViewModel.kakaoUser
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            kakaoAuthViewModel.removeNickname()
            navController.popBackStack()
        }

        binding.btnNext.setOnSingleClickListener {
            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    AuthApi.authService.isNotDuplicateNickname(NicknameRequest(binding.etNickname.text.toString()))
                }
                if (response.isSuccessful) {
                    kakaoAuthViewModel.setNickname(binding.etNickname.text.toString())
                    navController.navigate(R.id.action_kakaoNicknameFragment_to_kakaoSelectDisabilityFragment)
                } else {
                    binding.layoutNickname.error = "이미 존재하는 닉네임입니다."
                }
            }
        }

        binding.etNickname.addTextChangedListener {
            if (it != null) {
                if (it.length > 8) {
                    binding.btnNext.isEnabled = false
                    binding.layoutNickname.error = "닉네임은 최대 8글자까지 가능합니다."
                } else {
                    binding.btnNext.isEnabled = true
                    binding.layoutNickname.error = ""
                }
            }
        }
    }
}