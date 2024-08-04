package com.narae.fliwith.src.main.review

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.narae.fliwith.R
import com.narae.fliwith.databinding.FragmentReviewBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.review.models.Review
import com.narae.fliwith.src.main.review.models.ReviewViewModel

private const val TAG = "ReviewFragment_싸피"

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding
        get() = _binding!!

    private var reviewList = mutableListOf<Review>()
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var mainActivity: MainActivity

    private val viewModel: ReviewViewModel by activityViewModels()

    private var currentPage = 0
    private var isLoading = false
    private var currentOrder = "recent"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clearData()

        reviewAdapter = ReviewAdapter(requireContext(), reviewList)
        binding.reviewRv.apply {
            adapter = reviewAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1) && !isLoading) {
                        loadMoreReviews()
                    }
                }
            })
        }

        // 최신순 기본
        loadReviews(currentPage, currentOrder)
        binding.reviewCount.text = "${reviewList.size}개의 리뷰"

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

    private fun loadReviews(pageNo: Int, order: String) {
        isLoading = true
        viewModel.fetchSelectAllReviews(pageNo, order) { success, lastPageNo ->
            if (success) {
                observeReviewData(lastPageNo)
            } else {
                Log.d(TAG, "review 불러 오기 실패")
            }
            isLoading = false
        }
    }

    private fun loadMoreReviews() {
        currentPage++
        loadReviews(currentPage, currentOrder)
    }

    private fun observeReviewData(lastPageNo: Int) {
        viewModel.reviewDataResponse.observe(viewLifecycleOwner, Observer { reviewResponse ->
            reviewResponse?.data?.reviews?.let { reviews ->
                reviewList.addAll(reviews)
                reviewAdapter.updateReviews(reviewList)
                binding.reviewCount.text = "${reviewList.size}개의 리뷰"
            }
            // 마지막 페이지 여부 확인
            if (currentPage >= lastPageNo) {
                isLoading = true // 더 이상 로딩하지 않도록 설정
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
