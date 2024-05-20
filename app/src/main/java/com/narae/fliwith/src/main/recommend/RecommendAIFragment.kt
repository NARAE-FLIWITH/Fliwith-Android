package com.narae.fliwith.src.main.recommend

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentRecommendAIBinding
import com.narae.fliwith.src.main.recommend.dto.RecommendViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "RecommendAIFragment_싸피"
class RecommendAIFragment : BaseFragment<FragmentRecommendAIBinding>(FragmentRecommendAIBinding::inflate) {

    private var param1: String? = null
    private var param2: String? = null
    private val viewModel: RecommendViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchView()

    }

    private fun fetchView() {
        Log.d(TAG, "fetchView tourData : ${viewModel.tourData.value}")
        var responseData = viewModel.tourData.value?.data
        binding.aiTvCategory.text = viewModel.tourRequest.value?.contentType
        binding.aiTvName.text = responseData?.detailCommon?.title
        binding.aiTvUsetime.text = "연중무휴11111"
        binding.aiTvAddress.text = responseData?.detailCommon?.addr1
        Log.d(TAG, "fetchView firstimage1 : ${responseData?.detailCommon?.firstimage}")
        Log.d(TAG, "fetchView firstimage2 : ${responseData?.detailCommon?.firstimage2}")
        Glide.with(requireContext())
            .load("https://t1.daumcdn.net/daumtop_deco/images/pctop/2023/logo_daum.png")
            .into(binding.aiImgThumbnail)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecommendAIFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}