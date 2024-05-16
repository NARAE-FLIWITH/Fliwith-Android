package com.narae.fliwith.src.main.recommend

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.narae.fliwith.R
import com.narae.fliwith.databinding.FragmentMemberBinding
import com.narae.fliwith.databinding.FragmentTypeBinding
import com.narae.fliwith.src.main.MainActivity


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

// 인원 수 선택
class MemberFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentMemberBinding? = null
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
        _binding = FragmentMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.memberOneBtn.layoutRecommendSelectDetailBtn.text = "1인"
        binding.memberTwoBtn.layoutRecommendSelectDetailBtn.text = "2인"
        binding.memberThreeBtn.layoutRecommendSelectDetailBtn.text = "3인"
        binding.memberFourUpBtn.layoutRecommendSelectDetailBtn.text = "4인 이상"

        binding.memberOneBtn.layoutRecommendSelectDetailBtn.isSelected = true

        val buttons = arrayOf(
            binding.memberOneBtn,
            binding.memberTwoBtn,
            binding.memberThreeBtn,
            binding.memberFourUpBtn,
        )

        for (button in buttons) {
            button.layoutRecommendSelectDetailBtn.setOnClickListener {
                // 클릭된 버튼을 선택하고, 다른 버튼들을 비활성화
                button.layoutRecommendSelectDetailBtn.isSelected = true
                // 클릭된 버튼의 text를 selectedButtonText에 저장
                viewModel.setSelectedMemberButtonText(button.layoutRecommendSelectDetailBtn.text.toString())
                for (btn in buttons) {
                    if (btn != button) {
                        btn.layoutRecommendSelectDetailBtn.isSelected = false
                        btn.layoutRecommendSelectDetailBtn.isEnabled = true
                    } else {
                        btn.layoutRecommendSelectDetailBtn.isEnabled = false
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MemberFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}