package com.narae.fliwith.src.main.recommend

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.narae.fliwith.R
import com.narae.fliwith.databinding.FragmentRecommendBinding
import com.narae.fliwith.databinding.LayoutSelectAiBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.recommend.models.RecommendViewModel
import com.narae.fliwith.src.main.recommend.models.TourRequest
import com.narae.fliwith.util.DISABILITY
import com.narae.fliwith.util.changeColorStatusBar

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "RecommendFragment_싸피"
class RecommendFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentRecommendBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: RecommendViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecommendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window = requireActivity().window
        val context = requireContext()

        // 상태 바 색상 설정
        changeColorStatusBar(window, context, R.color.lavender, true)

        binding.regionBtn.layoutSelectAiText.text = "지역"
        binding.tourBtn.layoutSelectAiText.text = "여행지"
        binding.selectDisableBtn.layoutSelectAiText.text = "장애 선택"
        binding.memberBtn.layoutSelectAiText.text = "인원 수"
        binding.dateBtn.layoutSelectAiText.text = "날짜"

        val navController = findNavController()

        binding.regionBtn.layoutSelectAi.setOnClickListener{
            val bundle = bundleOf("message" to "region")
            navController.navigate(R.id.action_recommendFragment_to_recommendDetailFragment, bundle)
        }

        binding.tourBtn.layoutSelectAi.setOnClickListener{
            val bundle = bundleOf("message" to "type")
            navController.navigate(R.id.action_recommendFragment_to_recommendDetailFragment, bundle)
        }

        binding.selectDisableBtn.layoutSelectAi.setOnClickListener{
            val bundle = bundleOf("message" to "disable")
            navController.navigate(R.id.action_recommendFragment_to_recommendDetailFragment, bundle)
        }

        binding.memberBtn.layoutSelectAi.setOnClickListener{
            val bundle = bundleOf("message" to "member")
            navController.navigate(R.id.action_recommendFragment_to_recommendDetailFragment, bundle)
        }

        binding.dateBtn.layoutSelectAi.setOnClickListener{
            val bundle = bundleOf("message" to "date")
            navController.navigate(R.id.action_recommendFragment_to_recommendDetailFragment, bundle)
        }

        viewModel.selectedRegionButtonText.observe(viewLifecycleOwner) { updateButtonVisibility() }
        viewModel.selectedTypeButtonText.observe(viewLifecycleOwner) { updateButtonVisibility() }
        viewModel.selectedDisableButtonText.observe(viewLifecycleOwner) { updateButtonVisibility() }
        viewModel.selectedMemberButtonText.observe(viewLifecycleOwner) { updateButtonVisibility() }
        viewModel.selectDate.observe(viewLifecycleOwner) { updateButtonVisibility() }

        // 모든 데이터 다 들어 갔으면 그때 AI 추천 화면 으로
        binding.btnRecommendation.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("tourRequest", viewModel.tourRequest.value)
            }
            navController.navigate(R.id.action_recommendFragment_to_recommendSearchFragment, bundle)
        }

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun updateButtonVisibility() {
        val tourRequest = TourRequest(
            area = viewModel.selectedRegionButtonText.value ?: "",
            contentType = viewModel.selectedTypeButtonText.value ?: "",
            disability = DISABILITY.fromString(viewModel.selectedDisableButtonText.value ?: ""),
            peopleNum = viewModel.getPeopleNum(viewModel.selectedMemberButtonText.value ?: ""),
            visitedDate = viewModel.selectDate.value ?: ""
        )

        if(tourRequest.area.isNotEmpty()) {
            viewChange(binding.regionBtn, tourRequest.area)
        }
        if(tourRequest.contentType.isNotEmpty()) {
            viewChange(binding.tourBtn, tourRequest.contentType)
        }
        if(tourRequest.disability != DISABILITY.NOTSELECTED) {
            var disabilityText = DISABILITY.fromEnum(tourRequest.disability)
            viewChange(binding.selectDisableBtn, disabilityText)
        }
        if(tourRequest.peopleNum != 0) {
            var numberText = "${tourRequest.peopleNum}인"
            if(tourRequest.peopleNum==4) numberText = "4인 이상"
            viewChange(binding.memberBtn, numberText)
        }
        if(tourRequest.visitedDate.isNotEmpty()) {
            val dateParts = tourRequest.visitedDate.split("-")
            if (dateParts.size == 3) {
                val month = dateParts[1].toInt()
                val day = dateParts[2].toInt()
                val dateText = "${month}월 ${day}일"
                viewChange(binding.dateBtn, dateText)
            }
        }

        // 모든 값이 선택 되면
        if (tourRequest.area.isNotEmpty() &&
            tourRequest.contentType.isNotEmpty() &&
            tourRequest.disability != DISABILITY.NOTSELECTED &&
            tourRequest.peopleNum != 0 &&
            tourRequest.visitedDate.isNotEmpty()) {

            Log.d(TAG, "updateButtonVisibility: 전체 선택 완료")
            binding.btnRecommendationNonSelected.visibility = View.GONE
            binding.btnRecommendation.visibility = View.VISIBLE
            tourRequest.let {
                viewModel.setTourRequest(tourRequest)
            }
        } else { // 아직 다 선택 된 게 아니면
            binding.btnRecommendationNonSelected.visibility = View.VISIBLE
            binding.btnRecommendation.visibility = View.GONE
        }
    }

    private fun viewChange(binding : LayoutSelectAiBinding, text : String){
        binding.layoutSelectAiText.text = text
        binding.layoutSelectAiIv.visibility = View.GONE
        binding.layoutSelectAiText.setTextColor(ContextCompat.getColor(requireContext(), R.color.violet))
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val window = requireActivity().window
        val context = requireContext()

        // 상태 바 색상 설정
        changeColorStatusBar(window, context, R.color.white, true)

        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeSelectedInfo()
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