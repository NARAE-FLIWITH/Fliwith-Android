package com.narae.fliwith.src.main.recommend

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.narae.fliwith.R
import com.narae.fliwith.databinding.FragmentDateBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.recommend.models.RecommendViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


private const val TAG = "DateFragment_싸피"

class DateFragment : Fragment() {
    private var _binding: FragmentDateBinding? = null
    private val binding get() = _binding!!
    private var selectedDate: String? = null

    // 날짜 형식을 0000-00-00 로 변환
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

    private lateinit var mainActivity: MainActivity
    private val viewModel: RecommendViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // CalendarView 초기 날짜를 설정
        val initialDate = Calendar.getInstance()
        initialDate.timeInMillis = binding.calendarView.date

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // CalendarView에서 선택한 날짜를 받아오는 리스너
            val date = Calendar.getInstance()
            date.set(year, month, dayOfMonth)

            // 날짜 형식을 0000-00-00 로 변환
            selectedDate = dateFormat.format(date.time)
        }

        view.rootView.findViewById<AppCompatButton>(R.id.send_recommend_detail_btn)
            .setOnClickListener {
                // 날짜 문자열을 뷰모델에 저장
                selectedDate?.let {
                    viewModel.setSelectDate(it)
                }

                findNavController().popBackStack()
            }

        // 이전에 선택했던 날짜 보여주기
        val date = viewModel.selectDate.value
        if (!date.isNullOrBlank()) {
            dateFormat.parse(date)?.let { binding.calendarView.date = it.time }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DateFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
