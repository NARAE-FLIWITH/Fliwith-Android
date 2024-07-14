package com.narae.fliwith.src.auth.signup

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.narae.fliwith.R
import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentSelectDisabilityBinding
import com.narae.fliwith.src.auth.AuthApi.authService
import com.narae.fliwith.src.auth.AuthViewModel
import com.narae.fliwith.util.DISABILITY
import com.narae.fliwith.util.DISABILITY.*
import com.narae.fliwith.util.LoadingDialog
import com.narae.fliwith.util.setOnSingleClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "SelectDisabilityFragment"

class SelectDisabilityFragment :
    BaseFragment<FragmentSelectDisabilityBinding>(FragmentSelectDisabilityBinding::inflate) {
    private val viewModel by activityViewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            viewModel.removeDisability()
            navController.popBackStack()
        }

        binding.btnDisabled.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.btnNoneDisabled.isChecked = false
                binding.btnNotSelected.isChecked = false
                binding.btnNoneDisabled.visibility = View.INVISIBLE
                binding.btnNotSelected.visibility = View.GONE
                binding.layoutCategory.visibility = View.VISIBLE
            } else {
                binding.btnNoneDisabled.visibility = View.VISIBLE
                binding.btnNotSelected.visibility = View.VISIBLE
                binding.layoutCategory.visibility = View.GONE
                binding.groupCategory.clearCheck()
            }
            checkButtonState()
        }

        binding.btnNoneDisabled.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.btnDisabled.isChecked = false
                binding.btnNotSelected.isChecked = false
            }
            checkButtonState()
        }

        binding.btnNotSelected.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.btnDisabled.isChecked = false
                binding.btnNoneDisabled.isChecked = false
            }
            checkButtonState()
        }

        binding.btnHearing.setOnClickListener {
            checkButtonState()
        }
        binding.btnVisual.setOnClickListener {
            checkButtonState()
        }
        binding.btnPhysical.setOnClickListener {
            checkButtonState()
        }
        binding.checkboxPolicy.setOnCheckedChangeListener { _, _ ->
            checkButtonState()
        }
        binding.checkboxPrivacy.setOnCheckedChangeListener { _, _ ->
            checkButtonState()
        }
        binding.checkboxSensitive.setOnCheckedChangeListener { _, _ ->
            checkButtonState()
        }

        // 체크박스 터치 범위 확장
        binding.tvPrivacy.setOnClickListener {
            binding.checkboxPrivacy.isChecked = !binding.checkboxPrivacy.isChecked
            checkButtonState()
        }
        binding.tvPolicy.setOnClickListener {
            binding.checkboxPolicy.isChecked = !binding.checkboxPolicy.isChecked
            checkButtonState()
        }
        binding.tvSensitive.setOnClickListener {
            binding.checkboxSensitive.isChecked = !binding.checkboxSensitive.isChecked
            checkButtonState()
        }

        // 웹뷰
        binding.btnShowTermsPolicy.setOnClickListener {
            viewModel.setWebViewURL(ApplicationClass.POLICY_CONTRACT_URL)
            navController.navigate(R.id.action_selectDisabilityFragment_to_webViewFragment)
        }
        binding.btnShowTermsPrivacy.setOnClickListener {
            viewModel.setWebViewURL(ApplicationClass.PRIVACY_CONTRACT_URL)
            navController.navigate(R.id.action_selectDisabilityFragment_to_webViewFragment)
        }
        binding.btnShowTermsSensitive.setOnClickListener {
            viewModel.setWebViewURL(ApplicationClass.SENSITIVE_CONTRACT_URL)
            navController.navigate(R.id.action_selectDisabilityFragment_to_webViewFragment)
        }

        binding.btnNext.setOnSingleClickListener {
            mLoadingDialog.show()

            lateinit var selected: DISABILITY
            if (binding.btnDisabled.isChecked) {
                selected = if (binding.btnVisual.isChecked)
                    VISUAL
                else if (binding.btnHearing.isChecked)
                    HEARING
                else
                    PHYSICAL
            } else if (binding.btnNoneDisabled.isChecked) {
                selected = NONDISABLED
            } else {
                selected = NONE
            }
            viewModel.setDisability(selected)

            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    authService.signUp(viewModel.user)
                }
                if (response.isSuccessful) {
                    mLoadingDialog.dismiss()
                    navController.navigate(R.id.action_selectDisabilityFragment_to_sendMailFragment)
                } else {
                    mLoadingDialog.dismiss()
                    Log.d(TAG, "SignUp Error : ${response.errorBody()?.string()}")
                }
            }
        }
    }

    private fun checkButtonState() {
        binding.btnNext.isEnabled = (binding.btnNoneDisabled.isChecked ||
                binding.btnNotSelected.isChecked ||
                (binding.groupCategory.checkedRadioButtonId != -1)) &&
                binding.checkboxPolicy.isChecked &&
                binding.checkboxPrivacy.isChecked &&
                binding.checkboxSensitive.isChecked
    }
}