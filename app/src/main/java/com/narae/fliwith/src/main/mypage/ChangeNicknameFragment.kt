package com.narae.fliwith.src.main.mypage

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentChangeNicknameBinding
import com.narae.fliwith.src.main.mypage.MyPageApi.myPageService
import com.narae.fliwith.src.main.mypage.models.ChangeNicknameRequest
import com.narae.fliwith.util.setOnSingleClickListener
import com.narae.fliwith.util.showCustomSnackBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangeNicknameFragment :
    BaseFragment<FragmentChangeNicknameBinding>(FragmentChangeNicknameBinding::inflate) {

    private val profileViewModel by activityViewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etNickname.hint = profileViewModel.nickname

        setListeners()
    }

    private fun setListeners() {
        binding.etNickname.addTextChangedListener { text ->
            binding.btnNext.isEnabled = text.isNullOrBlank().not()
            binding.layoutNickname.error = ""
        }

        binding.btnNext.setOnSingleClickListener {
            if (networkUtil.isNetworkAvailable()) {
                lifecycleScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        myPageService.changeNickname(ChangeNicknameRequest(binding.etNickname.text.toString()))
                    }
                    if (response.isSuccessful) {
                        navController.popBackStack()
                        profileViewModel.nickname = binding.etNickname.text.toString()
                        showCustomSnackBar(requireContext(), binding.root, "닉네임이 변경되었습니다")
                    } else {
                        binding.btnNext.isEnabled = false
                        binding.layoutNickname.error = "중복된 닉네임입니다."
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }


    }
}