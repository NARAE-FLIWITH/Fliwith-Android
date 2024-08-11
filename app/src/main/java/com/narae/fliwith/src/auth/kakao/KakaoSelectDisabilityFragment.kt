package com.narae.fliwith.src.auth.kakao

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.user.UserApiClient
import com.narae.fliwith.R
import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentKakaoSelectDisabilityBinding
import com.narae.fliwith.src.auth.AuthApi
import com.narae.fliwith.src.auth.AuthApi.authService
import com.narae.fliwith.src.auth.models.KakaoSignUpRequest
import com.narae.fliwith.util.DISABILITY
import com.narae.fliwith.util.setOnSingleClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "KakaoSelectDisabilityFr"

class KakaoSelectDisabilityFragment : BaseFragment<FragmentKakaoSelectDisabilityBinding>(
    FragmentKakaoSelectDisabilityBinding::inflate
) {
    private val kakaoAuthViewModel by activityViewModels<KakaoAuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            kakaoAuthViewModel.removeDisability()
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
            kakaoAuthViewModel.setWebViewURL(ApplicationClass.POLICY_CONTRACT_URL)
            navController.navigate(R.id.action_kakaoSelectDisabilityFragment_to_kakaoWebViewFragment)
        }
        binding.btnShowTermsPrivacy.setOnClickListener {
            kakaoAuthViewModel.setWebViewURL(ApplicationClass.PRIVACY_CONTRACT_URL)
            navController.navigate(R.id.action_kakaoSelectDisabilityFragment_to_kakaoWebViewFragment)
        }
        binding.btnShowTermsSensitive.setOnClickListener {
            kakaoAuthViewModel.setWebViewURL(ApplicationClass.SENSITIVE_CONTRACT_URL)
            navController.navigate(R.id.action_kakaoSelectDisabilityFragment_to_kakaoWebViewFragment)
        }

        binding.btnNext.setOnSingleClickListener {
            mLoadingDialog.show()

            lateinit var selected: DISABILITY
            if (binding.btnDisabled.isChecked) {
                selected = if (binding.btnVisual.isChecked)
                    DISABILITY.VISUAL
                else if (binding.btnHearing.isChecked)
                    DISABILITY.HEARING
                else
                    DISABILITY.PHYSICAL
            } else if (binding.btnNoneDisabled.isChecked) {
                selected = DISABILITY.NONDISABLED
            } else {
                selected = DISABILITY.NONE
            }
            kakaoAuthViewModel.setDisability(selected)


            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e(TAG, "사용자 정보 요청 실패", error)
                } else if (user != null) {
                    lifecycleScope.launch {
                        val response = withContext(Dispatchers.IO) {
                            val kakaoUser = kakaoAuthViewModel.kakaoUser
                            authService.kakaoSignUp(
                                KakaoSignUpRequest(
                                    user.id!!, kakaoUser.nickname, kakaoUser.disability!!
                                )
                            )
                        }
                        // 회원가입 성공시
                        if (response.isSuccessful) {
                            mLoadingDialog.dismiss()
                            navController.navigate(R.id.action_kakaoSelectDisabilityFragment_to_kakaoCompleteFragment)
                            kakaoAuthViewModel.removeKakaoUser()
                        } else {
                            mLoadingDialog.dismiss()
                            Log.d(TAG, "SignUp Error : ${response.errorBody()?.string()}")
                        }
                    }
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