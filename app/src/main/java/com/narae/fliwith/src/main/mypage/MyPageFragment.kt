package com.narae.fliwith.src.main.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.user.UserApiClient
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentMyPageBinding
import com.narae.fliwith.src.auth.AuthActivity
import com.narae.fliwith.src.main.LoginViewModel
import com.narae.fliwith.src.main.mypage.MyPageApi.myPageService
import com.narae.fliwith.src.main.mypage.models.Profile
import com.narae.fliwith.src.main.review.models.ReviewViewModel
import com.narae.fliwith.util.DISABILITY.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MyPageFragment"

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::inflate) {
    private val reviewViewModel by activityViewModels<ReviewViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()
    private val profileViewModel by activityViewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        if (networkUtil.isNetworkAvailable())
            setProfile()
    }

    private fun setProfile() {
        lifecycleScope.launch {
            val response = withContext(Dispatchers.IO) {
                myPageService.getProfile()
            }
            if (response.isSuccessful) {
                setNameAndColor(response.body()!!.data)
                profileViewModel.disability = response.body()!!.data.disability
                profileViewModel.nickname = response.body()!!.data.nickname
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

        binding.layoutMypageBox.setOnClickListener {
            navController.navigate(R.id.action_menu_main_btm_nav_my_page_to_myPageDetailFragment)
        }

        // 내가 좋아요 한 리뷰
        binding.layoutLikeReview.setOnClickListener {
            reviewViewModel.fetchLikeReviews(0)
            val bundle = bundleOf().apply {
                putString("starting", "ilike")
            }
            navController.navigate(R.id.action_menu_main_btm_nav_my_page_to_ILikeFragment, bundle)
        }

        // 내가 쓴 리뷰
        binding.layoutWriteReview.setOnClickListener {
            reviewViewModel.fetchWriteReviews(0)
            val bundle = bundleOf().apply {
                putString("starting", "iwrite")
            }
            navController.navigate(R.id.action_menu_main_btm_nav_my_page_to_IWriteFragment, bundle)
        }
    }
}