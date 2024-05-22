package com.narae.fliwith.src.main.recommend

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentRecommendAIBinding
import com.narae.fliwith.src.main.recommend.dto.RecommendViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "RecommendAIFragment_싸피"
class RecommendAIFragment : BaseFragment<FragmentRecommendAIBinding>(FragmentRecommendAIBinding::inflate) {

    private val viewModel: RecommendViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchView()
        initViewPager()

    }

    private fun initViewPager() {
        // ViewPager2 Adapter setting
        var viewPagerAdatper = ViewPagerAdapter(requireActivity())
        viewPagerAdatper.addFragment(ViewPagerIntroductionFragment())
        viewPagerAdatper.addFragment(ViewPagerReviewFragment())

        // Adapter 연결
        binding.aiViewpager.apply {
            adapter = viewPagerAdatper

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }

        //ViewPager, TabLayout 연결
        TabLayoutMediator(binding.aiTabLayout, binding.aiViewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "소개"
                1 -> tab.text = "리뷰"
            }
        }.attach()
    }

    private fun fetchView() {
        Log.d(TAG, "fetchView tourData : ${viewModel.tourData.value}")
        var responseData = viewModel.tourData.value?.data
        binding.aiTvCategory.text = viewModel.tourRequest.value?.contentType
        binding.aiTvName.text = responseData?.detailCommon?.title
        binding.aiTvUsetime.text = "연중무휴"
        binding.aiTvAddress.text = responseData?.detailCommon?.addr1
        Log.d(TAG, "fetchView firstimage1 : ${responseData?.detailCommon?.firstimage}")
        Log.d(TAG, "fetchView firstimage2 : ${responseData?.detailCommon?.firstimage2}")
//        Glide.with(requireContext())
//            .load(responseData?.detailCommon?.firstimage)
//            .into(binding.aiImgThumbnail)
    }
}