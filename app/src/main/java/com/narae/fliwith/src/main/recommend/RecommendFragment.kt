package com.narae.fliwith.src.main.recommend

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.narae.fliwith.R
import com.narae.fliwith.databinding.FragmentRecommendBinding
import com.narae.fliwith.src.main.DESTINATION
import com.narae.fliwith.src.main.MainActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class RecommendFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentRecommendBinding? = null
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
        _binding = FragmentRecommendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.regionBtn.layoutSelectAiText.text = "지역"
        binding.tourBtn.layoutSelectAiText.text = "여행지"
        binding.selectDisableBtn.layoutSelectAiText.text = "장애 선택"
        binding.memberBtn.layoutSelectAiText.text = "인원 수"
        binding.dateBtn.layoutSelectAiText.text = "날짜"

        binding.regionBtn.layoutSelectAi.setOnClickListener{
            mainActivity.changeFragmentView(DESTINATION.RECOMMEND_DETAIL, "region")
        }

        // AI 추천 화면 으로
        binding.btnRecommendation.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecommendFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}