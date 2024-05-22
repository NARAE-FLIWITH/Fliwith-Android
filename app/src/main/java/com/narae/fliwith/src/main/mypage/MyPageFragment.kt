package com.narae.fliwith.src.main.mypage

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.narae.fliwith.R
import com.narae.fliwith.config.ApplicationClass.Companion.sharedPreferences
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.DialogProfileGuideBinding
import com.narae.fliwith.databinding.FragmentMyPageBinding
import com.narae.fliwith.src.auth.AuthActivity
import com.narae.fliwith.src.main.mypage.MyPageApi.myPageService
import com.narae.fliwith.src.main.mypage.models.Profile
import com.narae.fliwith.util.DISABILITY.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MyPageFragment"

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setProfile()
    }

    private fun setProfile() {
        lifecycleScope.launch {
            val response = withContext(Dispatchers.IO) {
                myPageService.getProfile()
            }
            if (response.isSuccessful) {
                setNameAndColor(response.body()!!.data)
            } else {
                Log.d(TAG, "setProfile Error: ${response.errorBody()?.string()}")
            }
        }

    }

    private fun setNameAndColor(profile: Profile) {
        val wingColor = when (profile.disability) {
            HEARING -> R.color.hearing
            VISUAL -> R.color.visual
            PHYSICAL -> R.color.physical
            NONDISABLED -> R.color.nonDisabled
            else -> R.color.none
        }

        val backgroundColor = when (profile.disability) {
            HEARING -> R.drawable.mypage_background_hearing
            VISUAL -> R.drawable.mypage_background_visual
            PHYSICAL -> R.drawable.mypage_background_physical
            NONDISABLED -> R.drawable.mypage_background_nondisabled
            else -> R.drawable.mypage_background_none
        }

        val category = when (profile.disability) {
            HEARING -> "청각장애"
            VISUAL -> "시각장애"
            PHYSICAL -> "지체장애"
            NONDISABLED -> "비장애"
            else -> "선택 안 함"
        }

        // 이름, 장애유형 설정
        binding.tvUsername.text = profile.nickname
        binding.tvCategory.text = category

        // 색상 설정
        binding.imgWing.setColorFilter(ContextCompat.getColor(requireContext(), wingColor))
        binding.layoutMypageBox.setBackgroundResource(backgroundColor)
    }

    private fun setListeners() {
        // 로그아웃
        binding.layoutLogout.setOnClickListener {
            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) { myPageService.logout() }
                if (response.isSuccessful) {
                    sharedPreferences.removeTokenData()
                    val intent = Intent(requireContext(), AuthActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Log.d(TAG, "myPage: 로그아웃 실패 ${response.errorBody()?.string()}")
                }
            }
        }

        binding.layoutMypageBox.setOnClickListener {
            val dialogView = DialogProfileGuideBinding.inflate(layoutInflater)

            val dialog = AlertDialog.Builder(requireContext()).setView(dialogView.root).show()
            dialogView.apply {
                btnClose.setOnClickListener {
                    dialog.dismiss()
                }
            }
            dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }

    }
}