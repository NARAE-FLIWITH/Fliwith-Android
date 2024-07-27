package com.narae.fliwith.src.auth.login

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentResetPasswordBinding
import com.narae.fliwith.src.auth.AuthApi.authService
import com.narae.fliwith.util.setOnSingleClickListener
import com.narae.fliwith.util.showCustomSnackBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ResetPasswordFragment :
    BaseFragment<FragmentResetPasswordBinding>(FragmentResetPasswordBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.btnNext.setOnSingleClickListener {
            if (binding.etEmail.text.isNullOrBlank() ||
                !Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()
            ) {
                showErrorText()
                return@setOnSingleClickListener
            }
            lifecycleScope.launch(Dispatchers.Main) {
                showLoadingDialog()
                val response = withContext(Dispatchers.IO) {
                    authService.temporaryPassword(binding.etEmail.text.toString())
                }
                dismissLoadingDialog()
                if (response.isSuccessful) {
                    navController.popBackStack()
                    showCustomSnackBar(requireContext(), binding.root, "임시 비밀번호가 발급되었습니다.")
                } else {
                    showErrorText()
                }
            }
        }
    }

    private fun showErrorText() {
        binding.tvHelper.visibility = View.GONE
        binding.tvError.visibility = View.VISIBLE
    }
}