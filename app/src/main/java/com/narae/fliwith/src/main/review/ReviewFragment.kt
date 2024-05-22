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
import androidx.recyclerview.widget.LinearLayoutManager
import com.narae.fliwith.R
import com.narae.fliwith.databinding.FragmentReviewBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.review.models.Review
import com.narae.fliwith.src.main.review.models.ReviewViewModel
import okhttp3.internal.immutableListOf

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "ReviewFragment_싸피"
class ReviewFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentReviewBinding? = null
    private val binding
        get() = _binding!!

    private var reviewList = immutableListOf<Review>()
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var mainActivity: MainActivity

    private val viewModel: ReviewViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reviewAdapter = ReviewAdapter(reviewList)
        binding.reviewRv.apply {
            adapter = reviewAdapter
        }

        // 최신순 기본
        viewModel.fetchSelectAllReviews(0, "recent") { success ->
            if (success) {
                observeReviewData()
            } else {
                Log.d(TAG, "onViewCreated: review 최신순 불러 오기 실패")
            }
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
            val popupMenu = PopupMenu(requireContext(), binding.reviewOrderList, 0, 0, R.style.CustomPopupMenu)
            val inflater: MenuInflater = popupMenu.menuInflater
            inflater.inflate(R.menu.menu_review_order_popup, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.new_menu -> {
                        binding.reviewOrderSelectText.text = "최신순"
                        viewModel.fetchSelectAllReviews(0, "recent") { success ->
                            if (success) {
                                observeReviewData()
                            } else {
                                Log.d(TAG, "onViewCreated: review 최신순 불러 오기 실패")
                            }
                        }
                    }
                    R.id.popular_menu -> {
                        binding.reviewOrderSelectText.text = "인기순"
                        viewModel.fetchSelectAllReviews(0, "like") { success ->
                            if (success) {
                                observeReviewData()
                            } else {
                                Log.d(TAG, "onViewCreated: review 인기순 불러 오기 실패")
                            }
                        }
                    }
                }
                false
            }
            popupMenu.show()
        }
    }

    private fun observeReviewData() {
        viewModel.reviewData.observe(viewLifecycleOwner, Observer { reviewResponse ->
            reviewResponse?.data?.reviews?.let { reviews ->
                reviewList = reviews
                reviewAdapter.updateReviews(reviews)
                binding.reviewCount.text = "${reviewList.size}개의 리뷰"
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReviewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
