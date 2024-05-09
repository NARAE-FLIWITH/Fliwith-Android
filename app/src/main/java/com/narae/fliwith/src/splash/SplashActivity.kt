package com.narae.fliwith.src.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.narae.fliwith.config.BaseActivity
import com.narae.fliwith.databinding.ActivitySplashBinding
import com.narae.fliwith.src.main.MainActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1500)
    }
}