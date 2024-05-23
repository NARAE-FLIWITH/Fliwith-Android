package com.narae.fliwith.src.main.review

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentRecommendAIBinding
import com.narae.fliwith.databinding.FragmentReviewDetailBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.review.models.ReviewViewModel
import com.narae.fliwith.util.userProfileImageConvert
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "ReviewDetailFragment_싸피"
class ReviewDetailFragment : BaseFragment<FragmentReviewDetailBinding>(FragmentReviewDetailBinding::inflate) {

    private lateinit var mainActivity: MainActivity
    private var reviewId:Int=-1

    private val viewModel: ReviewViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 받아온 reviewId
        reviewId = arguments?.getInt("reviewId") ?: -1

        // fetchSelectReview 호출 및 콜백에서 fetchData 호출
        viewModel.fetchSelectReview(reviewId) { success ->
            if (success) {
                fetchData()
            } else {
                Log.e(TAG, "Failed to fetch review details")
            }
        }

        // 일단 지우고
        binding.reviewDetailMenuIcon.visibility = View.GONE
    }

    private fun fetchData() {
        val response = viewModel.reviewDetailData.value?.data

        binding.reviewDetailUserName.text = response?.nickname

        // profile image
        response?.disability?.let { userProfileImageConvert(it, binding.reviewDetailProfileImage) }

        // response?.createdAt가 null일 경우 기본값 설정
        val timeCal = response?.createdAt?.let { timeCalculate(it) } ?: 0
        binding.reviewDetailTime.text = "$timeCal 시간전"
        binding.reviewHeartCount.text = response?.likes.toString()
        binding.reviewDetailPlace.text = response?.spotName
        binding.reviewDetailContent.text = response?.content

        Glide.with(requireContext())
            .load(response?.images?.get(0))
            .into(binding.reviewDefaultImage)

        // 수정, 삭제
        if(response?.mine==true) { // 내 게시물
            binding.reviewDetailMenuIcon.visibility = View.VISIBLE
            binding.reviewDetailMenuIcon.setOnClickListener {
                popUpMenu()
            }
        }else {
            binding.reviewDetailMenuIcon.visibility = View.GONE
        }

    }

    private fun popUpMenu() {
        val popupMenu = PopupMenu(requireContext(), binding.reviewDetailMenuIcon, 0, 0, R.style.CustomPopupMenu)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_review_detail_popup, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.update -> {
                    // 수정

                }
                R.id.delete -> {
                    // 삭제
                    viewModel.fetchDeleteReview(reviewId) {success ->
                        if(success) {
                            navController.navigate(R.id.action_reviewDetailFragment_to_menu_main_btm_nav_review)
                        }
                        else {
                            Log.e(TAG, "Failed to fetch review delete")
                        }
                    }
                }
            }
            false
        }
        popupMenu.show()
    }


    private fun timeCalculate(time: String): Long {
        // 서버에서 받아온 시간을 파싱
        val serverTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME)
        } else {
            // SDK 버전이 O 미만일 경우
            return 0
        }

        // 현재 시간을 구함
        val currentTime = LocalDateTime.now()

        // 시간을 비교하여 차이를 계산
        return ChronoUnit.HOURS.between(serverTime, currentTime)
    }

}