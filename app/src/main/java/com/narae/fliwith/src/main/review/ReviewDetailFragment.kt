package com.narae.fliwith.src.main.review

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.ItemContent
import com.kakao.sdk.template.model.ItemInfo
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.Social
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentReviewDetailBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.review.models.ReviewDetailData
import com.narae.fliwith.src.main.review.models.ReviewViewModel
import com.narae.fliwith.util.showCustomSnackBar
import com.narae.fliwith.util.userProfileImageConvert
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private const val TAG = "ReviewDetailFragment_싸피"

class ReviewDetailFragment :
    BaseFragment<FragmentReviewDetailBinding>(FragmentReviewDetailBinding::inflate) {

    private lateinit var mainActivity: MainActivity
    private var reviewId: Int = -1

    private val viewModel: ReviewViewModel by activityViewModels()
    private lateinit var response: ReviewDetailData

    // 이미지 Slider
    private lateinit var reviewSliderAdapter: ReviewSliderAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        init()
        heartStatus()
        back()
        setImageSlider()
        setShareLink()

    }

    private fun setShareLink() {
        // 카카오 공유 링크 생성
        binding.reviewDetailShareIcon.setOnClickListener {
            val defaultFeed = FeedTemplate(
                content = Content(
                    title = viewModel.reviewSpotName.value.toString(),
                    description = viewModel.reviewWriteContent.value.toString(),
                    imageUrl = viewModel.reviewImageUrls.value?.get(0).toString(),
                    link = Link(
                        webUrl = "https://developers.kakao.com",
                        mobileWebUrl = "https://developers.kakao.com",
                        androidExecutionParams = mapOf("reviewId" to "$reviewId"),
                        iosExecutionParams = mapOf("reviewId" to "$reviewId")
                    )
                ),
                buttons = listOf(
                    Button(
                        "게시물 확인하러 가기 🤩",
                        Link(
                            androidExecutionParams = mapOf("reviewId" to "$reviewId"),
                            iosExecutionParams = mapOf("reviewId" to "$reviewId")
                        )
                    )
                )
            )

            val isKakaoTalkAvailable = ShareClient.instance.isKakaoTalkSharingAvailable(requireContext())

            // 카카오톡 설치여부 확인
            if (isKakaoTalkAvailable) {
                // 카카오톡으로 공유
                ShareClient.instance.shareDefault(requireContext(), defaultFeed) { sharingResult, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오톡 공유 실패", error)
                    } else if (sharingResult != null) {
                        Log.d(TAG, "카카오톡 공유 성공 ${sharingResult.intent}")
                        startActivity(sharingResult.intent)

                        Log.w(TAG, "Warning Msg: ${sharingResult.warningMsg}")
                        Log.w(TAG, "Argument Msg: ${sharingResult.argumentMsg}")
                    }
                }
            } else {
                // 웹으로 공유
                val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultFeed)
                try {
                    KakaoCustomTabsClient.openWithDefault(requireContext(), sharerUrl)
                } catch (e: UnsupportedOperationException) {
                    Log.e(TAG, "CustomTabs 지원 브라우저가 없습니다.", e)
                    // CustomTabs 지원 브라우저가 없을 때, 기본 웹 브라우저로 열기
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl.toString()))
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Log.e(TAG, "인터넷 브라우저가 없습니다.", e)
                    }
                }
            }
        }
    }

    private fun init() {

        // 받아온 reviewId
        reviewId = arguments?.getInt("reviewId") ?: -1

        // 일단 지우고
        binding.reviewDetailMenuIcon.visibility = View.GONE

        // fetchSelectReview 호출 및 콜백에서 fetchData 호출
        viewModel.fetchSelectReview(reviewId) { success ->
            if (success) {
                fetchData()
            } else {
                Log.e(TAG, "Failed to fetch review details")
            }
        }

    }

    private fun heartStatus() {
        viewModel.reviewLikeStatus.observe(viewLifecycleOwner) { isLiked ->
            updateLike(isLiked!!)
        }

        // 좋아요 개수 관찰
        viewModel.reviewLikeCount.observe(viewLifecycleOwner) { count ->
            updateLikeCount(count!!)
        }
    }

    private fun back() {
        binding.reviewDetailBackIcon.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun setImageSlider() {
        // 이미지 슬라이더 설정
        reviewSliderAdapter = ReviewSliderAdapter(requireContext(), mutableListOf())
        binding.reviewDetailImageVp.adapter = reviewSliderAdapter
        binding.reviewDetailImageVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
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
        viewModel.fetchLikeReview(reviewId) { success ->
            if (success) {
                Log.d(TAG, "postLikeStatus: 좋아요 누르기 성공")
            } else {
                Log.e(TAG, "Failed to post review like")
            }
        }
    }

    private fun likeStatus(like: Boolean) {
        if (like) { // true 이미 좋아요 누른 상태
            viewModel.setReviewLikeStatue(true)
            binding.reviewHeartImage.visibility = View.VISIBLE
            binding.reviewHeartImageDisable.visibility = View.GONE
        } else {
            viewModel.setReviewLikeStatue(false)
            binding.reviewHeartImage.visibility = View.GONE
            binding.reviewHeartImageDisable.visibility = View.VISIBLE
        }
    }

    private fun fetchData() {
        response = viewModel.reviewDetailData.value?.data!!

        binding.reviewDetailUserName.text = response.nickname

        // profile image
        response.disability?.let { userProfileImageConvert(it, binding.reviewDetailProfileImage) }

        // response.createdAt가 null일 경우 기본값 설정
        val timeCal = response.createdAt?.let { timeCalculate(it) } ?: 0
        binding.reviewDetailTime.text = "$timeCal"
        binding.reviewHeartCount.text = response.likes.toString()

        binding.reviewDetailPlace.text = response.spotName
        binding.reviewDetailContent.text = response.content

        likeStatus(response.like)

        // 수정, 삭제
        if (response.mine) { // 내 게시물
            binding.reviewDetailMenuIcon.visibility = View.VISIBLE
            binding.reviewDetailMenuIcon.setOnClickListener {
                popUpMenu()
            }
            binding.reviewHeartImageDisable.setOnClickListener {
                showCustomSnackBar(requireContext(), binding.root, "내 게시물은 하트를 누를 수 없어요 😂")
            }
        } else {
            binding.reviewDetailMenuIcon.visibility = View.GONE
        }

        viewModel.setReviewLikeCount(response.likes.toString().toInt())
        viewModel.setSpotName(binding.reviewDetailPlace.text.toString())
        viewModel.setReviewWriteContent(binding.reviewDetailContent.text.toString())
        viewModel.setReviewImageUrls(response.images)
        viewModel.setSpotContentId(response.contentId)
        Log.d(TAG, "fetchData: contentId ${response.contentId}")

        Log.d(TAG, "fetchData: ${binding.reviewDetailPlace.text}, ${binding.reviewDetailContent.text}, ${response.images}")

        // 이미지 슬라이더에 이미지 URL들 설정
        reviewSliderAdapter.setImages(response.images)
        setupIndicators(response.images.size)
    }

    private fun setupIndicators(count: Int) {
        val indicators = arrayOfNulls<ImageView>(count)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(16, 8, 16, 8)
        }

        binding.reviewDetailImageIndicator.removeAllViews()

        for (i in indicators.indices) {
            indicators[i] = ImageView(context).apply {
                setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.review_detail_image_indicator_inactive))
                layoutParams = params
            }
            binding.reviewDetailImageIndicator.addView(indicators[i])
        }
        setCurrentIndicator(0)
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = binding.reviewDetailImageIndicator.childCount
        for (i in 0 until childCount) {
            val imageView = binding.reviewDetailImageIndicator.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.review_detail_image_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.review_detail_image_indicator_inactive
                    )
                )
            }
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
                            showCustomSnackBar(requireContext(), binding.root, "게시글 삭제를 실패 했습니다. 🥲")
                        }
                    }
                }
            }
            false
        }
        popupMenu.show()
    }

    private fun timeCalculate(time: String): String {
        // 서버에서 받아온 시간을 파싱
        val serverTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME)
        } else {
            // SDK 버전이 O 미만일 경우
            return "시간 정보를 사용할 수 없습니다."
        }

        // 현재 시간을 구함
        val currentTime = LocalDateTime.now()

        // 시간을 비교 하여 차이를 계산
        val diffInHours = ChronoUnit.HOURS.between(serverTime, currentTime)

        return if (diffInHours < 24) {
            Log.d(TAG, "timeCalculate: $diffInHours")
            "$diffInHours 시간 전"
        } else {
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
            serverTime.format(dateFormatter)
        }
    }

}
