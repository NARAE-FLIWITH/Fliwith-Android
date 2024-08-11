package com.narae.fliwith.src.main.review

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentReviewBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.review.models.Review
import com.narae.fliwith.src.main.review.models.ReviewViewModel

private const val TAG = "ReviewFragment_싸피"

class ReviewFragment : BaseFragment<FragmentReviewBinding>(
    FragmentReviewBinding::inflate
) {

    private var reviewList = mutableListOf<Review>()
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var mainActivity: MainActivity

    private val viewModel: ReviewViewModel by activityViewModels()

    private var currentPage = 0
    private var isLoading = false
    private var currentOrder = "recent"
    private var lastPageNo = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clearData()
        currentPage = 0
        reviewList.clear()

        reviewAdapter = ReviewAdapter(requireContext(), reviewList)

        // 최신순 기본
        loadReviews(currentPage, currentOrder)

        binding.reviewRv.apply {
            adapter = reviewAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1) && !isLoading && currentPage < lastPageNo) {
                        Log.d(
                            TAG,
                            "onScrolled: currentPage = ${currentPage}, lastPageNo = ${lastPageNo}"
                        )
                        loadMoreReviews()
                    }
                }
            })
        }

        val navController = findNavController()

        binding.reviewWriteBtn.setOnClickListener {
            navController.navigate(R.id.action_reviewFragment_to_reviewWriteFragment)
        }

        reviewAdapter.setItemClickListener(object : ReviewAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int) {
                val bundle = Bundle()
                bundle.putInt("reviewId", reviewList[position].reviewId)
                navController.navigate(R.id.action_reviewFragment_to_reviewDetailFragment, bundle)
            }
        })

        binding.reviewOrderList.setOnClickListener {
            val popupMenu =
                PopupMenu(requireContext(), binding.reviewOrderList, 0, 0, R.style.CustomPopupMenu)
            val inflater: MenuInflater = popupMenu.menuInflater
            inflater.inflate(R.menu.menu_review_order_popup, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                currentPage = 0
                reviewList.clear()
                when (menuItem.itemId) {
                    R.id.new_menu -> {
                        currentOrder = "recent"
                        binding.reviewOrderSelectText.text = "최신순"
                    }

                    R.id.popular_menu -> {
                        currentOrder = "like"
                        binding.reviewOrderSelectText.text = "인기순"
                    }
                }
                loadReviews(currentPage, currentOrder)
                false
            }
            popupMenu.show()
        }
    }

    private fun loadMoreReviews() {
        currentPage++
        Log.d(TAG, "loadMoreReviews: currentPage = ${currentPage}, lastPageNo = ${lastPageNo}")
        if (currentPage <= lastPageNo) loadReviews(currentPage, currentOrder)
    }

    private fun loadReviews(pageNo: Int, order: String) {
        Log.d(TAG, "loadReviews: pageNo = $pageNo")
        isLoading = true
        viewModel.fetchSelectAllReviews(pageNo, order) { success, fetchedLastPageNo, reviews ->
            if (success) {
                Log.d(TAG, "loadReviews: fetchedLastPageNo = $fetchedLastPageNo")
                lastPageNo = fetchedLastPageNo // 마지막 페이지 번호

                // reviewList 업데이트
                reviewList.addAll(reviews)
                reviewAdapter.updateReviews(reviewList)
                binding.reviewCount.text = "${reviewList.size}개의 리뷰"
                isLoading = false // 데이터 로드가 완료되면 isLoading을 false로 설정
            } else {
                isLoading = false
            }
        }
    }
}
