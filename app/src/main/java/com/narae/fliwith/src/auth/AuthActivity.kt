package com.narae.fliwith.src.auth

import android.os.Bundle
import androidx.activity.viewModels
import com.narae.fliwith.config.BaseActivity
import com.narae.fliwith.databinding.ActivityAuthBinding

class AuthActivity : BaseActivity<ActivityAuthBinding>(ActivityAuthBinding::inflate) {

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}