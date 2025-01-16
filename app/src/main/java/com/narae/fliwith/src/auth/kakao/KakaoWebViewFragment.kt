package com.narae.fliwith.src.auth.kakao

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentKakaoWebViewBinding

class KakaoWebViewFragment : BaseFragment<FragmentKakaoWebViewBinding>(
    FragmentKakaoWebViewBinding::inflate
) {
    private val kakaoAuthViewModel by activityViewModels<KakaoAuthViewModel>()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webView = binding.webView
        webView.settings.apply {
            javaScriptEnabled = true
        }
        webView.loadUrl(kakaoAuthViewModel.getWebViewURL())

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }
    }
}