package com.narae.fliwith.src.main.recommend

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.narae.fliwith.R
import com.narae.fliwith.databinding.FragmentRecommendDetailBinding
import com.narae.fliwith.src.main.DESTINATION
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.home.HomeFragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val TAG = "RecommendDetailFragment_싸피"
class RecommendDetailFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentRecommendDetailBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
//            Log.d(TAG, "onCreate: $param1")
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecommendDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var transaction = childFragmentManager.beginTransaction()

        // 지역 페이지
        if(param1=="region") {
            binding.pageText.text = "지역 선택"
            transaction.replace(R.id.recommend_detail_fr, RegionFragment())
            transaction.commit()
        }

        // 여행지 유형 페이지
        else if(param1=="type") {
            binding.pageText.text = "여행지 유형 선택"
            transaction.replace(R.id.recommend_detail_fr, TypeFragment())
            transaction.commit()
        }

        else if(param1=="disable") {
            binding.pageText.text = "장애 유형 선택"
            transaction.replace(R.id.recommend_detail_fr, DisableFragment())
            transaction.commit()
        }

        // 인원 수 선택 페이지
        else if(param1=="member") {
            binding.pageText.text = "인원 수 선택"
            transaction.replace(R.id.recommend_detail_fr, MemberFragment())
            transaction.commit()
        }

        // 날짜 선택 페이지
        else if(param1=="date") {
            binding.pageText.text = "날짜 선택"
            transaction.replace(R.id.recommend_detail_fr, DateFragment())
            transaction.commit()
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecommendDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}