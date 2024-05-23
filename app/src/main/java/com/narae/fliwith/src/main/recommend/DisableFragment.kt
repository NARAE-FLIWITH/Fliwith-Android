package com.narae.fliwith.src.main.recommend

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.narae.fliwith.R
import com.narae.fliwith.databinding.FragmentDisableBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.recommend.models.RecommendViewModel


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DisableFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentDisableBinding? = null
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
        _binding = FragmentDisableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.disableBlindBtn.layoutRecommendSelectDetailBtn.text = "시각장애"
        binding.disableDeafBtn.layoutRecommendSelectDetailBtn.text = "청각장애"
        binding.disablePhysicalBtn.layoutRecommendSelectDetailBtn.text = "지체장애"
        binding.disableNotDisabilityBtn.layoutRecommendSelectDetailBtn.text = "비장애"
        binding.disableNotSelectedBtn.layoutRecommendSelectDetailBtn.text = "선택 안 함"

        val buttons = arrayOf(
            binding.disableBlindBtn,
            binding.disableDeafBtn,
            binding.disablePhysicalBtn,
            binding.disableNotDisabilityBtn,
            binding.disableNotSelectedBtn,
        )

        for (button in buttons) {
            button.layoutRecommendSelectDetailBtn.setOnClickListener {
                for (btn in buttons) {
                    // 클릭된 버튼을 선택하고, 다른 버튼들을 비활성화
                    if (btn != button) {
                        btn.layoutRecommendSelectDetailBtn.isSelected = false
                        btn.layoutRecommendSelectDetailBtn.isEnabled = true
                    } else {
                        btn.layoutRecommendSelectDetailBtn.isSelected = true
                        btn.layoutRecommendSelectDetailBtn.isEnabled = false
                    }
                }
            }

            // 이전에 선택했던 항목을 선택 상태로 보여주기 위함
            val selected = viewModel.selectedDisableButtonText.value
            if (selected == button.layoutRecommendSelectDetailBtn.text) {
                button.layoutRecommendSelectDetailBtn.isSelected = true
                button.layoutRecommendSelectDetailBtn.isEnabled = false
            }
        }

        view.rootView.findViewById<AppCompatButton>(R.id.send_recommend_detail_btn)
            .setOnClickListener {
                // 클릭된 버튼의 text를 selectedButtonText에 저장
                for (btn in buttons) {
                    if (btn.layoutRecommendSelectDetailBtn.isSelected)
                        viewModel.setSelectedDisableButtonText(btn.layoutRecommendSelectDetailBtn.text.toString())
                }
                findNavController().popBackStack()
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DisableFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}