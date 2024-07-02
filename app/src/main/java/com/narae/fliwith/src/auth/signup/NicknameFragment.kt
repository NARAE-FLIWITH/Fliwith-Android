package com.narae.fliwith.src.auth.signup

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentNicknameBinding
import com.narae.fliwith.src.auth.AuthApi.authService
import com.narae.fliwith.src.auth.AuthViewModel
import com.narae.fliwith.src.auth.models.NicknameRequest
import com.narae.fliwith.util.setOnSingleClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NicknameFragment : BaseFragment<FragmentNicknameBinding>(FragmentNicknameBinding::inflate) {

    private val viewModel by activityViewModels<AuthViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.removeNickname()
                navController.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        binding.user = viewModel.user
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            viewModel.removeNickname()
            navController.popBackStack()
        }

        binding.btnNext.setOnSingleClickListener {
            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    authService.isNotDuplicateNickname(NicknameRequest(binding.etNickname.text.toString()))
                }
                if (response.isSuccessful) {
                    viewModel.setNickname(binding.etNickname.text.toString())
                    navController.navigate(R.id.action_nicknameFragment_to_selectDisabilityFragment)
                } else {
                    binding.layoutNickname.error = "이미 존재하는 닉네임입니다."
                }
            }
        }

        binding.etNickname.addTextChangedListener {
            if (it.isNullOrBlank()) {
                binding.btnNext.isEnabled = false
                binding.layoutNickname.error = "이미 존재하는 닉네임입니다."
            } else {
                binding.btnNext.isEnabled = true
                binding.layoutNickname.error = ""
            }
        }
    }


}