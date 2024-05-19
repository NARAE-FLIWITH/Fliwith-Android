package com.narae.fliwith.src.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.narae.fliwith.config.BaseActivity
import com.narae.fliwith.databinding.ActivitySplashBinding
import com.narae.fliwith.src.auth.AuthActivity


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setFullScreen()
        delaySplash(1500)

        // 만약 회원정보가 있다면 바로 메인으로 넘기기

    }

    private fun delaySplash(delayMillis: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }, delayMillis)
    }

    private fun setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.apply {
                setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                )
            }
        } else {
            val windowInsetsController = WindowInsetsControllerCompat(
                window, window.decorView
            )
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        }
    }
}
