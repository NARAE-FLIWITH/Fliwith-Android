package com.narae.fliwith.src.auth.signup

import android.os.Bundle
import android.view.View
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentCompleteBinding

class CompleteFragment : BaseFragment<FragmentCompleteBinding>(FragmentCompleteBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.setOnClickListener {
            navController.navigate(R.id.action_completeFragment_to_loginHomeFragment)
        }
    }
}