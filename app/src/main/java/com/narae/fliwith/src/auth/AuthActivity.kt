package com.narae.fliwith.src.auth

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseActivity
import com.narae.fliwith.databinding.ActivityAuthBinding

class AuthActivity : BaseActivity<ActivityAuthBinding>(ActivityAuthBinding::inflate) {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}