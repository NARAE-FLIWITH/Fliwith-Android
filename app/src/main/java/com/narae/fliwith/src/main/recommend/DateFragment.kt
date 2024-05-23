package com.narae.fliwith.src.main.recommend

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.narae.fliwith.databinding.FragmentDateBinding
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.recommend.models.RecommendViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "DateFragment_싸피"
class DateFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentDateBinding? = null
    private val binding get() = _binding!!
    
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

        // 날짜 형식을 0000-00-00 로 변환
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val initialDateString = dateFormat.format(initialDate.time)

        // 초기 날짜 문자열을 ViewModel에 설정
        // 현재 날짜로
        viewModel.setSelectDate(initialDateString)

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // CalendarView에서 선택한 날짜를 받아오는 리스너
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)

            // 날짜 형식을 0000-00-00 로 변환
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateString = dateFormat.format(selectedDate.time)

            // 날짜 문자열을 로그로 출력
            viewModel.setSelectDate(dateString)
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
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
