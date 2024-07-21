package com.narae.fliwith.src.main.recommend

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentViewPagerReviewBinding
import com.narae.fliwith.src.main.recommend.models.RecommendViewModel
import com.narae.fliwith.src.main.recommend.models.ViewPagerReview

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

// view pager review page
private const val TAG = "ViewPagerReviewFragment_싸피"
class ViewPagerReviewFragment : BaseFragment<FragmentViewPagerReviewBinding>(
    FragmentViewPagerReviewBinding::inflate) {

    private var viewPagerReviewList = mutableListOf<ViewPagerReview>()
    private lateinit var viewPagerReviewAdapter : ViewPagerReviewAdapter

    private val viewModel: RecommendViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerReviewAdapter = ViewPagerReviewAdapter(viewPagerReviewList)
        binding.viewPagerReviewRv.adapter = viewPagerReviewAdapter

        fetchView()
    }

    private fun fetchView() {
        Log.d(TAG, "fetchView reviewData : ${viewModel.tourData.value}")
        var responseData = viewModel.tourData.value?.data

        viewModel.tourData.observe(viewLifecycleOwner, Observer { tourData ->
            val reviews = responseData?.reviews?.map { review ->
                ViewPagerReview(review.image, review.nickname)
            } ?: emptyList()

            viewPagerReviewAdapter.updateData(reviews)
        })

    }
}