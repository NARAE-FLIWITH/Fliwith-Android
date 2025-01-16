package com.narae.fliwith.src.auth.signup

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentSendMailBinding
import com.narae.fliwith.src.auth.AuthApi.authService
import com.narae.fliwith.src.auth.AuthViewModel
import com.narae.fliwith.util.setOnSingleClickListener
import com.narae.fliwith.util.showCustomSnackBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "SendMailFragment"

class SendMailFragment : BaseFragment<FragmentSendMailBinding>(FragmentSendMailBinding::inflate) {
    private val authViewModel by activityViewModels<AuthViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                authViewModel.removeUser()
                navController.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnSingleClickListener {
            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    authService.isAuthenticatedEmail(authViewModel.user.email)
                }

                if (response.isSuccessful) {
                    authViewModel.removeUser()
                    navController.navigate(R.id.action_sendMailFragment_to_completeFragment)
                } else {
                    showCustomSnackBar(requireContext(), binding.root, "이메일 인증을 완료해 주세요")
                    Log.d(TAG, "Email Authentication Error : ${response.errorBody()?.string()}")
                }
            }
        }
    }
}