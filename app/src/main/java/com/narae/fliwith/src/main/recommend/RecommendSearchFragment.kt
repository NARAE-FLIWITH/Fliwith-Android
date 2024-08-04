package com.narae.fliwith.src.main.recommend

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentRecommendSearchBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.recommend.models.RecommendViewModel
import com.narae.fliwith.util.changeColorStatusBar
import com.narae.fliwith.util.showCustomSnackBar


private const val TAG = "RecommendSearchFragment_싸피"
class RecommendSearchFragment : BaseFragment<FragmentRecommendSearchBinding>(FragmentRecommendSearchBinding::inflate) {

    private val viewModel: RecommendViewModel by activityViewModels()

    // 여기서 API 연결 해서 받은 값을 다음 화면 에서 보여 주기
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.tourRequest.value?.let { request ->
            viewModel.fetchTourData(request) { success ->
                if (success) {
                    navController.navigate(R.id.action_recommendSearchFragment_to_recommendAIFragment)
                } else {
                    navController.popBackStack()
                    showCustomSnackBar(requireContext(), binding.root, "잠시 후 다시 시도해 주세요")
                    Log.d(TAG, "onViewCreated: 데이터를 받아오지 못함")
                }
            }
        }

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }

    }

    override fun onResume() {
        super.onResume()

        val window = requireActivity().window
        val context = requireContext()

        // 상태 바 색상 설정
        changeColorStatusBar(window, context, R.color.lavender, true)
    }

}