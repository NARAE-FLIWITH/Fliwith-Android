package com.narae.fliwith.src.main.recommend

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.narae.fliwith.databinding.FragmentRecommendSearchBinding
import com.narae.fliwith.src.main.DISABILITY
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.recommend.dto.RecommendViewModel
import com.narae.fliwith.src.main.recommend.dto.TourRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "RecommendSearchFragment_싸피"
class RecommendSearchFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentRecommendSearchBinding? = null
    private val binding
        get() = _binding!!

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecommendSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    // 여기서 API 연결 해서 받은 값을 다음 화면 에서 보여 주기
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        fetchTourData(tourRequest)
    }

    private fun fetchTourData(request: TourRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RecommendApi.recommendService.selectAll(request)
                if (response.isSuccessful) {
                    val tourData = response.body()
                    Log.d("MainActivity", "Tour Data: $tourData")
                } else {
                    Log.e("MainActivity", "Response not successful: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "API call failed", e)
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