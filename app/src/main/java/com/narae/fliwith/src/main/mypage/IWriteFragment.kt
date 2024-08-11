package com.narae.fliwith.src.main.mypage

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentIWriteBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.review.ReviewAdapter
import com.narae.fliwith.src.main.review.models.Review
import com.narae.fliwith.src.main.review.models.ReviewViewModel

class IWriteFragment : BaseFragment<FragmentIWriteBinding>(
    FragmentIWriteBinding::inflate
) {

    private var reviewList = mutableListOf<Review>()
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var mainActivity: MainActivity

    private var currentPage = 0
    private var isLoading = false
    private var lastPageNo = 0

    private val viewModel: ReviewViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()

        currentPage = 0
        reviewList.clear()

        reviewAdapter = ReviewAdapter(requireContext(), reviewList)

        // pageNo=0 데이터 List
        loadReviews(currentPage)

        binding.reviewRv.apply {
            adapter = reviewAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1) && !isLoading && currentPage < lastPageNo) {
                        loadMoreReviews()
                    }
                }
            })
        }

        val navController = findNavController()

        reviewAdapter.setItemClickListener(object : ReviewAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int) {
                val bundle = Bundle()
                bundle.putInt("reviewId", reviewList[position].reviewId)
                navController.navigate(R.id.action_IWriteFragment_to_reviewDetailFragment, bundle)
            }
        })
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadMoreReviews() {
        currentPage++
        if(currentPage <= lastPageNo) loadReviews(currentPage)
    }

    private fun loadReviews(pageNo: Int) {
        isLoading = true
        viewModel.fetchWriteReviews(pageNo) { success, fetchedLastPageNo, reviews ->
            if (success) {
                lastPageNo = fetchedLastPageNo // 마지막 페이지 번호

                // reviewList 업데이트
                reviewList.addAll(reviews)
                reviewAdapter.updateReviews(reviewList)
                isLoading = false // 데이터 로드가 완료되면 isLoading을 false로 설정
            } else {
                isLoading = false
            }
        }
    }
}
