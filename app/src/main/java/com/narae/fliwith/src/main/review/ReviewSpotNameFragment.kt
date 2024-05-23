package com.narae.fliwith.src.main.review

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentReviewSpotNameBinding
import com.narae.fliwith.src.main.review.models.Review
import com.narae.fliwith.src.main.review.models.ReviewSpotName
import com.narae.fliwith.src.main.review.models.ReviewViewModel
import okhttp3.internal.immutableListOf

private const val TAG = "ReviewSpotNameFragment_싸피"
class ReviewSpotNameFragment : BaseFragment<FragmentReviewSpotNameBinding>(
FragmentReviewSpotNameBinding::inflate) {

    private lateinit var reviewSpotNameAdapter: ReviewSpotNameAdapter
    private var reviewSpotNameList = immutableListOf<ReviewSpotName>()
    private val viewModel: ReviewViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 지역 선택 하고 확인 누르면 (지역 : name)
        fetchView()

        // response 받은 값 list 에 대입
        reviewSpotNameAdapter = ReviewSpotNameAdapter(requireContext(), reviewSpotNameList)
        binding.reviewSpotNameRv.apply {
            adapter = reviewSpotNameAdapter
        }
        Log.d(TAG, "reviewSpotNameList : $reviewSpotNameList")

        // 돌려 받은 값 중에 하나 선택 하면 이전 화면 으로 이동


    }

    private fun fetchView() {
        viewModel.fetchSpotName("부산") {success ->
            if(success) {
                viewModel.reviewSpotNameResponse.value?.data?.let {
                    reviewSpotNameList = it
                    reviewSpotNameAdapter.updateData(reviewSpotNameList)
                }
            }else{
                Log.d(TAG, "review spot name 불러 오기 실패")
            }
        }
    }
}