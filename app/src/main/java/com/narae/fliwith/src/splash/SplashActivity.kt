package com.narae.fliwith.src.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.narae.fliwith.config.BaseActivity
import com.narae.fliwith.databinding.ActivitySplashBinding
import com.narae.fliwith.src.auth.AuthActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }, 1500)

        setContentView(binding.root)
    }
}
