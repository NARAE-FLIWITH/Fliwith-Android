package com.narae.fliwith.src.main.review

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentReviewSpotNameBinding
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

        binding.reviewWriteSearchSpotEt.setOnTouchListener { v, event ->
            val drawableRight = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.reviewWriteSearchSpotEt.right - binding.reviewWriteSearchSpotEt.compoundDrawables[drawableRight].bounds.width())) {
                    // 아이콘 클릭 시 fetchView 호출
                    fetchView()
                    return@setOnTouchListener true
                }
            }
            false
        }

        // response 받은 값 list 에 대입
        reviewSpotNameAdapter = ReviewSpotNameAdapter(requireContext(), reviewSpotNameList)
        binding.reviewSpotNameRv.apply {
            adapter = reviewSpotNameAdapter
        }
        Log.d(TAG, "reviewSpotNameList : $reviewSpotNameList")

        // 돌려 받은 값 중에 하나 선택 하면 이전 화면 으로 이동
        reviewSpotNameAdapter.setItemClickListener(object : ReviewSpotNameAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int) {
                // viewModel 에 spotName 저장
                viewModel.setSpotName(reviewSpotNameList[position].name)
                viewModel.setSpotContentId(reviewSpotNameList[position].contentId)
                Log.d(TAG, "onClick search icon review spot : ${reviewSpotNameList[position].name}, ${reviewSpotNameList[position].contentId}")
                navController.navigate(R.id.action_reviewSpotNameFragment_to_reviewWriteFragment)
            }
        })

    }

    private fun fetchView() {
        viewModel.fetchSpotName(binding.reviewWriteSearchSpotEt.text.toString()) {success ->
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