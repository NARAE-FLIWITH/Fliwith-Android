package com.narae.fliwith.src.main.recommend

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.activityViewModels
import com.narae.fliwith.R
import com.narae.fliwith.databinding.FragmentRecommendBinding
import com.narae.fliwith.databinding.FragmentRegionBinding
import com.narae.fliwith.src.main.MainActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RegionFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentRegionBinding? = null
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
        _binding = FragmentRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.Seoul.isSelected = true
        viewModel.setSelectedRegionButtonText("서울")

        val buttons = arrayOf(
            binding.Seoul,
            binding.Gyeonggi,
            binding.Incheon,
            binding.Gangwon,
            binding.Daejeon,
            binding.Sejong,
            binding.Jeonnam,
            binding.Jeonbuk,
            binding.Chungnam,
            binding.Chungbuk,
            binding.Gwangju,
            binding.Gyeongbuk,
            binding.Daegu,
            binding.Busan,
            binding.Jeju
        )

        for (button in buttons) {
            button.setOnClickListener {
                // 클릭된 버튼을 선택하고, 다른 버튼들을 비활성화
                button.isSelected = true
                // 클릭된 버튼의 text를 selectedButtonText에 저장
                viewModel.setSelectedRegionButtonText(button.text.toString())
                for (btn in buttons) {
                    if (btn != button) {
                        btn.isSelected = false
                        btn.isEnabled = true
                    } else {
                        btn.isEnabled = false
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}