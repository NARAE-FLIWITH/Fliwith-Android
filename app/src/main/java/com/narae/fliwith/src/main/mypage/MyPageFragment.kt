package com.narae.fliwith.src.main.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.narae.fliwith.R
import com.narae.fliwith.config.ApplicationClass
import com.narae.fliwith.config.ApplicationClass.Companion.sharedPreferences
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentMyPageBinding
import com.narae.fliwith.src.auth.AuthActivity
import com.narae.fliwith.src.main.mypage.MyPageApi.myPageService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MyPageFragment"

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(FragmentMyPageBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        // 로그아웃
        binding.layoutLogout.setOnClickListener {
            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO){ myPageService.logout()}
                if (response.isSuccessful) {
                    sharedPreferences.removeTokenData()
                    val intent = Intent(requireContext(), AuthActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Log.d(TAG, "myPage: 로그아웃 실패 ${response.errorBody()?.string()}")
                }
            }
        }
    }
}