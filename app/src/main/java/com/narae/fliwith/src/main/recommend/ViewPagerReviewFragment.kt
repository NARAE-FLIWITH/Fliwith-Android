package com.narae.fliwith.src.main.recommend

import android.os.Bundle
import android.view.View
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentViewPagerReviewBinding
import com.narae.fliwith.src.main.recommend.dto.ViewPagerReview

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


// view pager review page
class ViewPagerReviewFragment : BaseFragment<FragmentViewPagerReviewBinding>(
    FragmentViewPagerReviewBinding::inflate) {

    private var viewPagerReviewList = mutableListOf<ViewPagerReview>()
    private lateinit var viewPagerReviewAdapter : ViewPagerReviewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (i in 1..10) {
            viewPagerReviewList.add(
                ViewPagerReview(
                    null, "여기서 리뷰 보고 갔는데 자연 만끽하며\n" +
                            "다니기 참 좋은 것 같아요! 추천합니다 :)")
            )
        }
        viewPagerReviewAdapter = ViewPagerReviewAdapter(viewPagerReviewList)
        binding.viewPagerReviewRv.adapter = viewPagerReviewAdapter
    }
}