package com.narae.fliwith.src.main.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.narae.fliwith.R
import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.config.ApplicationClass.Companion.sharedPreferences
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentHomeBinding
import com.narae.fliwith.src.auth.models.TokenData
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.home.models.Store
import com.narae.fliwith.src.main.home.models.Tour
import com.narae.fliwith.util.changeColorStatusBar

// TODO: Rename parameter arguments, choose names that match
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private var storeList = mutableListOf<Store>()
    private var tourList = mutableListOf<Tour>()

    private lateinit var storeAdapter: HomeStoreAdapter
    private lateinit var tourAdapter: HomeTourAdapter
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window = requireActivity().window
        val context = requireContext()
        // 상태 바 색상 설정
        changeColorStatusBar(window, context, R.color.white, true)

        for (i in 1..10) {
            storeList.add(Store(storeName = "식당1"))
        }

        storeList = mutableListOf(
            Store(R.drawable.store2, "라플란드 베이커리"),
            Store(R.drawable.store3, "글로리 페어몬트"),
            Store(R.drawable.store1, "애월더힐 브런치 카페"),
            Store(R.drawable.store4, "합정 르프리크"),
        )
        storeAdapter = HomeStoreAdapter(storeList)
        binding.storeRv.adapter = storeAdapter

        tourList = mutableListOf(
            Tour(R.drawable.tour1, "고불총림 백양사"),
            Tour(R.drawable.tour2, "고령군 가야 꽃길"),
            Tour(R.drawable.tour3, "경복궁"),
            Tour(R.drawable.tour4, "영암 바람의 언덕")
        )
        tourAdapter = HomeTourAdapter(tourList)
        binding.tourRv.adapter = tourAdapter

        val navController = findNavController()

        binding.btnRecommendation.setOnClickListener {
            mainActivity.viewGone()
            navController.navigate(R.id.action_homeFragment_to_recommendFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        val window = requireActivity().window
        val context = requireContext()
        // 상태 바 색상 설정
        changeColorStatusBar(window, context, R.color.white, true)
        mainActivity.viewVisible()
    }
}