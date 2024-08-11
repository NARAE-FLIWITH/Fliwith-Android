package com.narae.fliwith.src.main.recommend

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentRecommendDetailBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.util.changeColorStatusBar

private const val TAG = "RecommendDetailFragment_싸피"

class RecommendDetailFragment : BaseFragment<FragmentRecommendDetailBinding>(
    FragmentRecommendDetailBinding::inflate
) {
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var transaction = childFragmentManager.beginTransaction()

        val message = arguments?.getString("message")

        val window = requireActivity().window
        val context = requireContext()
        // 상태 바 색상 설정
        changeColorStatusBar(window, context, R.color.white, true)

        // 지역 페이지
        if (message == "region") {
            binding.pageText.text = "지역 선택"
            transaction.replace(R.id.recommend_detail_fr, RegionFragment())
            transaction.commit()
        }

        // 여행지 유형 페이지
        else if (message == "type") {
            binding.pageText.text = "여행지 유형 선택"
            transaction.replace(R.id.recommend_detail_fr, TypeFragment())
            transaction.commit()
        } else if (message == "disable") {
            binding.pageText.text = "장애 유형 선택"
            transaction.replace(R.id.recommend_detail_fr, DisableFragment())
            transaction.commit()
        }

        // 인원 수 선택 페이지
        else if (message == "member") {
            binding.pageText.text = "인원 수 선택"
            transaction.replace(R.id.recommend_detail_fr, MemberFragment())
            transaction.commit()
        }

        // 날짜 선택 페이지
        else if (message == "date") {
            binding.pageText.text = "날짜 선택"
            transaction.replace(R.id.recommend_detail_fr, DateFragment())
            transaction.commit()
        }

        val navController = findNavController()

        binding.sendRecommendDetailBtn.setOnClickListener {
            navController.popBackStack()
        }

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }
    }

}