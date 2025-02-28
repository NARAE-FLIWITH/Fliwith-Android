package com.narae.fliwith.src.main.recommend

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentRecommendAIBinding
import com.narae.fliwith.src.main.recommend.models.RecommendViewModel
import com.narae.fliwith.util.changeColorStatusBar

private const val TAG = "RecommendAIFragment_싸피"

class RecommendAIFragment :
    BaseFragment<FragmentRecommendAIBinding>(FragmentRecommendAIBinding::inflate) {

    private val viewModel: RecommendViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchView()
        initViewPager()
        setListeners()
        setFromMap()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navController.popBackStack()
                }
            })
    }

    override fun onResume() {
        super.onResume()

        val window = requireActivity().window
        val context = requireContext()

        // 상태 바 색상 설정
        changeColorStatusBar(window, context, R.color.white, true)
    }

    private fun setFromMap() {
        if (arguments?.getBoolean("fromMap") == true) {
            binding.appBar.visibility = View.GONE
            binding.btnBackMap.visibility = View.VISIBLE
        } else {
            binding.appBar.visibility = View.VISIBLE
            binding.btnBackMap.visibility = View.GONE
        }
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.btnBackMap.setOnClickListener {
            navController.popBackStack()
        }
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
        binding.aiTvUsetime.text = responseData?.detailCommon?.addr1
        binding.aiTvAddress.text = "우편번호(" + responseData?.detailCommon?.zipcode + ")"

        if (responseData?.detailCommon?.firstimage?.isNotEmpty() == true) {
            Glide.with(requireContext())
                .load(responseData.detailCommon.firstimage)
//                .placeholder(R.drawable.placeholder)
                .error(R.drawable.no_image)
                .into(binding.aiImgThumbnail)
        }
    }

}