package com.narae.fliwith.src.main.recommend

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentViewPagerIntroductionBinding
import com.narae.fliwith.src.main.recommend.models.RecommendViewModel


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "ViewPagerIntroductionFr_싸피"
class ViewPagerIntroductionFragment : BaseFragment<FragmentViewPagerIntroductionBinding>(
    FragmentViewPagerIntroductionBinding::inflate) {

    private val viewModel: RecommendViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchView()

    }

    private fun fetchView() {
        Log.d(TAG, "fetchView tourData : ${viewModel.tourData.value}")
        var responseData = viewModel.tourData.value?.data

//        binding.introductionAddressTv.text = responseData?.detailCommon?.addr1
//        binding.introductionNumberTv.text = responseData?.detailCommon?.tel
//        binding.introductionUrlTv.text = responseData?.detailCommon?.homepage
    }

}