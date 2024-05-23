package com.narae.fliwith.src.main.review

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.narae.fliwith.R
import com.narae.fliwith.databinding.FragmentIWriteBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.review.models.Review
import com.narae.fliwith.src.main.review.models.ReviewViewModel
import okhttp3.internal.immutableListOf

class IWriteFragment : Fragment() {

    private var _binding: FragmentIWriteBinding? = null
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()

        reviewAdapter = ReviewAdapter(requireContext(), reviewList)
        binding.reviewRv.apply {
            adapter = reviewAdapter
        }

        val navController = findNavController()

        reviewAdapter.setItemClickListener(object : ReviewAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int) {
                val bundle = Bundle()
                bundle.putInt("reviewId", reviewList[position].reviewId)
                navController.navigate(R.id.action_IWriteFragment_to_reviewDetailFragment, bundle)
            }
        })

        observeReviewData()
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeReviewData() {
        viewModel.reviewDataResponse.observe(viewLifecycleOwner, Observer { reviewResponse ->
            reviewResponse?.data?.reviews?.let { reviews ->
                reviewList = reviews
                reviewAdapter.updateReviews(reviews)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
