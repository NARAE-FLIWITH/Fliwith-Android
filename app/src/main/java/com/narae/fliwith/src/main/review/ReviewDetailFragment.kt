package com.narae.fliwith.src.main.review

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentRecommendAIBinding
import com.narae.fliwith.databinding.FragmentReviewDetailBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.review.models.ReviewViewModel
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
    }

    private fun initView() {

    }

    private fun fetchData() {
        val response = viewModel.reviewDetailData.value?.data

        binding.reviewDetailUserName.text = response?.nickname

        // response?.createdAt가 null일 경우 기본값 설정
        val timeCal = response?.createdAt?.let { timeCalculate(it) } ?: 0
        binding.reviewDetailTime.text = "$timeCal 시간전"
        binding.reviewHeartCount.text = response?.likes.toString()
        binding.reviewDetailPlace.text = response?.spotName
        binding.reviewDetailContent.text = response?.content
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