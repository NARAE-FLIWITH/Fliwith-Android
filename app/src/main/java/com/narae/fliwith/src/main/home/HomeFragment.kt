package com.narae.fliwith.src.main.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.narae.fliwith.R
import com.narae.fliwith.databinding.FragmentHomeBinding
import com.narae.fliwith.src.main.DESTINATION
import com.narae.fliwith.src.main.MainActivity
import com.narae.fliwith.src.main.home.models.Store
import com.narae.fliwith.src.main.home.models.Tour

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    private var storeList = mutableListOf<Store>()
    private var tourList = mutableListOf<Tour>()

    private lateinit var storeAdapter : HomeStoreAdapter
    private lateinit var tourAdapter : HomeTourAdapter

    private lateinit var mainActivity: MainActivity

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (i in 1..10) {
            storeList.add(Store(imageUrl = null, "식당1"))
        }
        storeAdapter = HomeStoreAdapter(storeList)
        binding.storeRv.adapter = storeAdapter

        for (i in 1..10) {
            tourList.add(Tour(imageUrl = null, "관광지 이름"))
        }
        tourAdapter = HomeTourAdapter(tourList)
        binding.tourRv.adapter = tourAdapter

        val navController = findNavController()

        binding.btnRecommendation.setOnClickListener{
            navController.navigate(R.id.action_homeFragment_to_recommendFragment)
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}