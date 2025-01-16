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
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentRegionBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.recommend.models.RecommendViewModel

class RegionFragment : BaseFragment<FragmentRegionBinding>(
    FragmentRegionBinding::inflate
) {

    private lateinit var mainActivity: MainActivity
    private val viewModel: RecommendViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            binding.Gyeongnam,
            binding.Ulsan,
            binding.Chungbuk,
            binding.Gwangju,
            binding.Gyeongbuk,
            binding.Daegu,
            binding.Busan,
            binding.Jeju
        )

        for (button in buttons) {
            button.setOnClickListener {
                // 클릭된 버튼의 text를 selectedButtonText에 저장
                for (btn in buttons) {
                    // 클릭된 버튼을 선택하고, 다른 버튼들을 비활성화
                    if (btn != button) {
                        btn.isSelected = false
                        btn.isEnabled = true
                    } else {
                        btn.isSelected = true
                        btn.isEnabled = false
                    }
                }
            }

            // 이전에 선택했던 항목을 선택 상태로 보여주기 위함
            val selected = viewModel.selectedRegionButtonText.value
            if (selected == button.text) {
                button.isSelected = true
                button.isEnabled = false
            }
        }

        view.rootView.findViewById<AppCompatButton>(R.id.send_recommend_detail_btn)
            .setOnClickListener {
                // 클릭된 버튼의 text를 selectedButtonText에 저장
                for (btn in buttons) {
                    if (btn.isSelected)
                        viewModel.setSelectedRegionButtonText(btn.text.toString())
                }
                findNavController().popBackStack()
            }
    }
}