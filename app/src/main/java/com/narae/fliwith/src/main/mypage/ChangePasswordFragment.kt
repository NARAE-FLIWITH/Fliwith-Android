package com.narae.fliwith.src.main.mypage

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentChangePasswordBinding
import com.narae.fliwith.src.main.mypage.MyPageApi.myPageService
import com.narae.fliwith.src.main.mypage.models.ChangePasswordRequest
import com.narae.fliwith.util.setOnSingleClickListener
import com.narae.fliwith.util.showCustomSnackBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangePasswordFragment :
    BaseFragment<FragmentChangePasswordBinding>(FragmentChangePasswordBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.etPwOld.addTextChangedListener {
            if (!it.isNullOrEmpty() && it.toString() == binding.etPwNew.text.toString()) {
                binding.layoutPwNew.error = "현재 비밀번호와 같은 비밀번호로 변경할 수 없습니다."
            } else {
                binding.layoutPwNew.error = ""
            }
            binding.layoutPwOld.error = ""
            checkButtonEnabled()
        }

        binding.etPwNew.addTextChangedListener {
            if (!it.isNullOrEmpty() && it.toString() == binding.etPwOld.text.toString()) {
                binding.layoutPwNew.error = "현재 비밀번호와 같은 비밀번호로 변경할 수 없습니다."
            } else {
                binding.layoutPwNew.error = ""
            }

            if (!it.isNullOrEmpty() && !binding.etPwRepeat.text.isNullOrEmpty() && binding.etPwNew.text.toString() != binding.etPwRepeat.text.toString()) {
                binding.layoutPwRepeat.error = "비밀번호가 일치하지 않습니다."
            } else {
                binding.layoutPwRepeat.error = ""
            }
            checkButtonEnabled()
        }

        binding.etPwRepeat.addTextChangedListener {
            if (!it.isNullOrEmpty() && binding.etPwNew.text.toString() != binding.etPwRepeat.text.toString()) {
                binding.layoutPwRepeat.error = "비밀번호가 일치하지 않습니다."
            } else {
                binding.layoutPwRepeat.error = ""
            }
            checkButtonEnabled()
        }

        binding.btnNext.setOnSingleClickListener {
            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    myPageService.changePassword(
                        ChangePasswordRequest(
                            binding.etPwOld.text.toString(),
                            binding.etPwNew.text.toString()
                        )
                    )
                }

                if (response.isSuccessful) {
                    navController.popBackStack()
                    showCustomSnackBar(requireContext(), binding.root, "비밀번호가 변경되었습니다.")
                } else {
                    binding.layoutPwOld.error = "비밀번호가 올바르지 않습니다."
                }
            }
        }
    }

    private fun checkButtonEnabled() {
        val pattern = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}\$")
        with(binding) {
            btnNext.isEnabled =
                (layoutPwOld.error.isNullOrEmpty() && binding.etPwOld.text?.isNotEmpty() == true
                        && layoutPwNew.error.isNullOrEmpty() && binding.etPwNew.text?.isNotEmpty() == true
                        && layoutPwRepeat.error.isNullOrEmpty() && binding.etPwRepeat.text?.isNotEmpty() == true
                        && pattern.matches(etPwNew.text.toString()))
        }
    }
}