package com.narae.fliwith.src.main.mypage

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.user.UserApiClient
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.DialogProfileGuideBinding
import com.narae.fliwith.databinding.FragmentMyPageDetailBinding
import com.narae.fliwith.src.auth.AuthActivity
import com.narae.fliwith.src.main.LoginViewModel
import com.narae.fliwith.src.main.mypage.models.Profile
import com.narae.fliwith.util.DISABILITY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MyPageDetailFragment_싸피"
class MyPageDetailFragment :
    BaseFragment<FragmentMyPageDetailBinding>(FragmentMyPageDetailBinding::inflate) {
    private val profileViewModel by activityViewModels<ProfileViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProfile(Profile(profileViewModel.disability, profileViewModel.nickname))
        setListeners()

        // 카카오 회원은 비밀번호 변경 불가
        if (AuthApiClient.instance.hasToken()) {
            binding.layoutPw.visibility = View.GONE
        }
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.cardWing.setOnClickListener {
            val dialogView = DialogProfileGuideBinding.inflate(layoutInflater)

            val dialog = AlertDialog.Builder(requireContext()).setView(dialogView.root).show()
            dialogView.apply {
                btnClose.setOnClickListener {
                    dialog.dismiss()
                }
            }
            dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }

        binding.layoutNickname.setOnClickListener {
            navController.navigate(R.id.action_myPageDetailFragment_to_changeNicknameFragment)
        }

        binding.layoutPw.setOnClickListener {
            navController.navigate(R.id.action_myPageDetailFragment_to_changePasswordFragment)
        }

        binding.layoutResign.setOnClickListener {
            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) { MyPageApi.myPageService.logout() }
                // 카카오 로그아웃
                if (AuthApiClient.instance.hasToken()) {
                    UserApiClient.instance.logout { error ->
                        if (error != null) {
                            Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                        } else {
                            Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                        }
                    }
                }

                loginViewModel.logout()
                val intent = Intent(requireContext(), AuthActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)

            }
        }
    }

    private fun setProfile(profile: Profile) {
        val wingColor = when (profile.disability) {
            DISABILITY.HEARING -> R.color.hearing
            DISABILITY.VISUAL -> R.color.visual
            DISABILITY.PHYSICAL -> R.color.physical
            DISABILITY.NONDISABLED -> R.color.nonDisabled
            else -> R.color.none
        }

        // 이름, 장애유형 설정
        binding.tvNickname.text = profile.nickname

        // 색상 설정
        binding.imgWing.setColorFilter(ContextCompat.getColor(requireContext(), wingColor))
    }

}
