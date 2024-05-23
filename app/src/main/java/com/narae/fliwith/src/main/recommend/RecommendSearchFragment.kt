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

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "RecommendSearchFragment_싸피"
class RecommendSearchFragment : BaseFragment<FragmentRecommendSearchBinding>(FragmentRecommendSearchBinding::inflate) {


    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mainActivity: MainActivity
    private val viewModel: RecommendViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    // 여기서 API 연결 해서 받은 값을 다음 화면 에서 보여 주기
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.tourRequest.value?.let { request ->
            viewModel.fetchTourData(request) { success ->
                if (success) {
                    navController.navigate(R.id.action_recommendSearchFragment_to_recommendAIFragment)
                } else {
                    Log.d(TAG, "onViewCreated: 데이터를 받아오지 못함")
                }
            }
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecommendSearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}