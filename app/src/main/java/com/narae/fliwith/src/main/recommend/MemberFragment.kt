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
import com.narae.fliwith.databinding.FragmentMemberBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.recommend.models.RecommendViewModel


// 인원 수 선택
class MemberFragment : BaseFragment<FragmentMemberBinding>(
    FragmentMemberBinding::inflate
) {

    private lateinit var mainActivity: MainActivity
    private val viewModel: RecommendViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.memberOneBtn.layoutRecommendSelectDetailBtn.text = "1인"
        binding.memberTwoBtn.layoutRecommendSelectDetailBtn.text = "2인"
        binding.memberThreeBtn.layoutRecommendSelectDetailBtn.text = "3인"
        binding.memberFourUpBtn.layoutRecommendSelectDetailBtn.text = "4인 이상"

        val buttons = arrayOf(
            binding.memberOneBtn,
            binding.memberTwoBtn,
            binding.memberThreeBtn,
            binding.memberFourUpBtn,
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
            val selected = viewModel.selectedMemberButtonText.value
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
                        viewModel.setSelectedMemberButtonText(btn.layoutRecommendSelectDetailBtn.text.toString())
                }
                findNavController().popBackStack()
            }
    }
}