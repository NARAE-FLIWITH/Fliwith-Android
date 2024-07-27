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
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentRecommendAIBinding
import com.narae.fliwith.databinding.FragmentReviewDetailBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.review.models.ReviewDetailData
import com.narae.fliwith.src.main.review.models.ReviewDetailResponse
import com.narae.fliwith.src.main.review.models.ReviewViewModel
import com.narae.fliwith.util.userProfileImageConvert
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "ReviewDetailFragment_싸피"

class ReviewDetailFragment :
    BaseFragment<FragmentReviewDetailBinding>(FragmentReviewDetailBinding::inflate) {

    private lateinit var mainActivity: MainActivity
    private var reviewId: Int = -1

    private val viewModel: ReviewViewModel by activityViewModels()
    private lateinit var response: ReviewDetailData

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 받아온 reviewId
        reviewId = arguments?.getInt("reviewId") ?: -1

        viewModel.reviewLikeStatus.observe(viewLifecycleOwner) { isLiked ->
            updateLike(isLiked!!)
        }

        // 좋아요 개수 관찰
        viewModel.reviewLikeCount.observe(viewLifecycleOwner) { count ->
            updateLikeCount(count!!)
        }

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

        binding.reviewDetailBackIcon.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun updateLike(isLiked: Boolean) {
        if (isLiked) { // 좋아요 누른 상태 라면
            // 좋아요 버튼 누르면
            binding.reviewHeartImage.setOnClickListener {
                // 좋아요 취소 상태로
                binding.reviewHeartImageDisable.visibility = View.VISIBLE
                binding.reviewHeartImage.visibility = View.GONE

                viewModel.setReviewLikeStatue(false)
                val count = viewModel.reviewLikeCount.value ?: 0
                viewModel.setReviewLikeCount(count - 1)
                postLikeStatus()
            }
        } else { // 좋아요 누른 상태가 아니면
            // 비어 있는 좋아요 버튼 누르면
            binding.reviewHeartImageDisable.setOnClickListener {
                // 좋아요 상태로
                binding.reviewHeartImageDisable.visibility = View.GONE
                binding.reviewHeartImage.visibility = View.VISIBLE

                viewModel.setReviewLikeStatue(true)
                val count = viewModel.reviewLikeCount.value ?: 0
                viewModel.setReviewLikeCount(count + 1)
                postLikeStatus()
            }
        }
    }

    private fun updateLikeCount(count: Int) {
        // 좋아요 개수를 UI에 업데이트
        binding.reviewHeartCount.text = count.toString()
    }


    private fun postLikeStatus() {
        viewModel.fetchLikeReview(reviewId) {success ->
            if(success) {
                Log.d(TAG, "postLikeStatus: 좋아요 누르기 성공")
            }
            else{
                Log.e(TAG, "Failed to post review like")
            }
        }
    }

    private fun likeStatus(like: Boolean) {
        if(like) { // true 이미 좋아요 누른 상태
            viewModel.setReviewLikeStatue(true)
            binding.reviewHeartImage.visibility = View.VISIBLE
            binding.reviewHeartImageDisable.visibility = View.GONE
        }else {
            viewModel.setReviewLikeStatue(false)
            binding.reviewHeartImage.visibility = View.GONE
            binding.reviewHeartImageDisable.visibility = View.VISIBLE
        }
    }

    private fun fetchData() {
        response = viewModel.reviewDetailData.value?.data!!

        binding.reviewDetailUserName.text = response?.nickname

        // profile image
        response?.disability?.let { userProfileImageConvert(it, binding.reviewDetailProfileImage) }

        // response?.createdAt가 null일 경우 기본값 설정
        val timeCal = response?.createdAt?.let { timeCalculate(it) } ?: 0
        binding.reviewDetailTime.text = "$timeCal 시간전"
        binding.reviewHeartCount.text = response?.likes.toString()

        binding.reviewDetailPlace.text = response?.spotName
        binding.reviewDetailContent.text = response?.content


        likeStatus(response.like)

        Glide.with(requireContext())
            .load(response?.images?.get(0))
            .error(R.drawable.no_image)
            .placeholder(R.drawable.placeholder)
            .into(binding.reviewDefaultImage)

        // 수정, 삭제
        if (response?.mine == true) { // 내 게시물
            binding.reviewDetailMenuIcon.visibility = View.VISIBLE
            binding.reviewDetailMenuIcon.setOnClickListener {
                popUpMenu()
            }
        } else {
            binding.reviewDetailMenuIcon.visibility = View.GONE
        }

        // 데이터 미리 넣어 두기
        viewModel.setReviewLikeCount(response?.likes.toString().toInt())
        viewModel.setSpotName(binding.reviewDetailPlace.text.toString())
        viewModel.setReviewWriteContent(binding.reviewDetailContent.text.toString())
        viewModel.setImageUrl(response.images[0])
        viewModel.setSpotContentId(response?.contentId!!)
        Log.d(TAG, "fetchData: contentId ${response?.contentId!!}")

        Log.d(TAG, "fetchData: ${binding.reviewDetailPlace.text}, ${binding.reviewDetailContent.text}, ${response.images[0]}")

    }

    private fun popUpMenu() {
        val popupMenu =
            PopupMenu(requireContext(), binding.reviewDetailMenuIcon, 0, 0, R.style.CustomPopupMenu)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_review_detail_popup, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.update -> {
                    // 수정
                    val bundle = Bundle()
                    bundle.putInt("reviewId", reviewId)
                    navController.navigate(R.id.action_reviewDetailFragment_to_reviewWriteFragment, bundle)
                }

                R.id.delete -> {
                    // 삭제
                    viewModel.fetchDeleteReview(reviewId) { success ->
                        if (success) {
                            navController.popBackStack()
                        } else {
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