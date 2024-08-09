package com.narae.fliwith.src.main.recommend

import ViewPagerReviewAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentViewPagerReviewBinding
import com.narae.fliwith.src.main.recommend.models.RecommendViewModel
import com.narae.fliwith.src.main.recommend.models.ViewPagerReview

// view pager review page
private const val TAG = "ViewPagerReviewFragment_싸피"

class ViewPagerReviewFragment : BaseFragment<FragmentViewPagerReviewBinding>(
    FragmentViewPagerReviewBinding::inflate) {

    private var viewPagerReviewList = mutableListOf<ViewPagerReview>()
    private lateinit var viewPagerReviewAdapter: ViewPagerReviewAdapter

    private val viewModel: RecommendViewModel by activityViewModels()
    private var isLoading = false
    private var currentPage = 0
    private var lastPage = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerReviewAdapter = ViewPagerReviewAdapter(viewPagerReviewList)
        binding.viewPagerReviewRv.adapter = viewPagerReviewAdapter
        binding.viewPagerReviewRv.layoutManager = LinearLayoutManager(context)

        binding.viewPagerReviewRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && !isLoading && !lastPage) {
                    loadMoreReviews()
                }
            }
        })

        fetchView()
    }

    private fun fetchView() {
        val contentId = viewModel.tourData.value?.data?.detailCommon?.contentid ?: return
        fetchReviews(contentId, 0)
    }

    private fun fetchReviews(contentId: String, pageNo: Int) {
        isLoading = true
        viewModel.fetchTourReviewData(pageNo, contentId) { success, tourReviewResponse ->
            isLoading = false
            if (success && tourReviewResponse != null) {
                if (pageNo == 0) {
                    viewPagerReviewList.clear()
                }
                val reviews = tourReviewResponse.data.reviews.map { review ->
                    ViewPagerReview(review.image, review.content)
                }
                viewPagerReviewList.addAll(reviews)
                viewPagerReviewAdapter.notifyDataSetChanged()
                lastPage = tourReviewResponse.data.pageNo == tourReviewResponse.data.lastPageNo
                currentPage = tourReviewResponse.data.pageNo + 1
            } else {
                Log.e(TAG, "Failed to fetch tour review data")
            }
        }
    }

    private fun loadMoreReviews() {
        val contentId = viewModel.tourData.value?.data?.detailCommon?.contentid ?: return
        fetchReviews(contentId, currentPage)
    }
}
