package com.narae.fliwith.src.main.review

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentRecommendAIBinding
import com.narae.fliwith.databinding.FragmentReviewDetailBinding
import com.narae.fliwith.src.main.MainActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReviewDetailFragment : BaseFragment<FragmentReviewDetailBinding>(FragmentReviewDetailBinding::inflate) {

    private lateinit var mainActivity: MainActivity
    private var reviewId:Int=-1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 받아온 reviewId
        reviewId = arguments?.getInt("reviewId") ?: -1

        

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReviewDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}