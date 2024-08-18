package com.narae.fliwith.src.main.recommend

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentRecommendSearchBinding
import com.narae.fliwith.src.main.recommend.models.RecommendViewModel
import com.narae.fliwith.util.changeColorStatusBar
import com.narae.fliwith.util.showCustomSnackBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private const val TAG = "RecommendSearchFragment_싸피"

class RecommendSearchFragment :
    BaseFragment<FragmentRecommendSearchBinding>(FragmentRecommendSearchBinding::inflate) {

    private val viewModel: RecommendViewModel by activityViewModels()
    private var isActive = false

    override fun onResume() {
        super.onResume()
        val window = requireActivity().window
        val context = requireContext()

        // 상태 바 색상 설정
        changeColorStatusBar(window, context, R.color.lavender, true)
    }

    // 여기서 API 연결 해서 받은 값을 다음 화면 에서 보여 주기
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window = requireActivity().window
        val context = requireContext()
        isActive = true

        // 상태 바 색상 설정
        changeColorStatusBar(window, context, R.color.lavender, true)

        val textView: TextView = binding.recommendSearchTv
        lifecycleScope.launch {
            while (true) { // 무한 반복
                textView.text = "찾아보는 중 ."
                delay(500) // 0.5초 대기
                textView.text = "찾아보는 중 . ."
                delay(500) // 0.5초 대기
                textView.text = "찾아보는 중 . . ."
                delay(500) // 0.5초 대기
                textView.text = "찾아보는 중"
                delay(500) // 0.5초 대기
            }
        }

        viewModel.tourRequest.value?.let { request ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.fetchTourData(request) { success ->
                    if (isActive) {
                        if (success) {
                            navController.navigate(R.id.action_recommendSearchFragment_to_recommendAIFragment)
                        } else {
                            navController.popBackStack()
                            showCustomSnackBar(requireContext(), binding.root, "잠시 후 다시 시도해 주세요")
                            Log.d(TAG, "onViewCreated: 데이터를 받아오지 못함")
                        }
                    } else {
                        Log.d(TAG, "Fragment is no longer active, ignoring callback.")
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isActive = false // 뷰가 파괴되었음을 명시
    }

}