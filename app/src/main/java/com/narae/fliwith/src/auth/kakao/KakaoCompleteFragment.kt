package com.narae.fliwith.src.auth.kakao

import android.os.Bundle
import android.view.View
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentKakaoCompleteBinding

class KakaoCompleteFragment :
    BaseFragment<FragmentKakaoCompleteBinding>(FragmentKakaoCompleteBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.setOnClickListener {
            TODO("로그인 api 호출하고 바로 홈으로 보내기")
        }
    }
}