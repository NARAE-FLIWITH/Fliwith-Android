package com.narae.fliwith.src.auth.signup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentWebViewBinding
import com.narae.fliwith.src.auth.AuthViewModel

class WebViewFragment : BaseFragment<FragmentWebViewBinding>(FragmentWebViewBinding::inflate) {
    private val authViewModel by activityViewModels<AuthViewModel>()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webView = binding.webView
        webView.settings.apply {
            javaScriptEnabled = true
        }
        webView.loadUrl(authViewModel.getWebViewURL())

        binding.btnBack.setOnClickListener {
            navController.popBackStack()
        }
    }
}