package com.narae.fliwith.src.main.recommend

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentTypeBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.recommend.models.RecommendViewModel

// 여행지 유형 선택
class TypeFragment : BaseFragment<FragmentTypeBinding>(
    FragmentTypeBinding::inflate
) {

    private lateinit var mainActivity: MainActivity
    private val viewModel: RecommendViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.typeAllBtn.layoutRecommendSelectDetailBtn.text = "전체"
        binding.typeTourBtn.layoutRecommendSelectDetailBtn.text = "관광지"
        binding.typeCulturalBtn.layoutRecommendSelectDetailBtn.text = "문화시설"
        binding.typeLodgmentBtn.layoutRecommendSelectDetailBtn.text = "숙박"
        binding.typeShoppingBtn.layoutRecommendSelectDetailBtn.text = "쇼핑"
        binding.typeStoreBtn.layoutRecommendSelectDetailBtn.text = "음식점"

        val buttons = arrayOf(
            binding.typeAllBtn,
            binding.typeShoppingBtn,
            binding.typeStoreBtn,
            binding.typeLodgmentBtn,
            binding.typeCulturalBtn,
            binding.typeTourBtn
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
            val selected = viewModel.selectedTypeButtonText.value
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
                        viewModel.setSelectedTypeButtonText(btn.layoutRecommendSelectDetailBtn.text.toString())
                }
                findNavController().popBackStack()
            }
    }

}