package com.narae.fliwith.src.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.narae.fliwith.config.ApplicationClass.Companion.sharedPreferences
import com.narae.fliwith.config.BaseActivity
import com.narae.fliwith.databinding.ActivitySplashBinding
import com.narae.fliwith.src.auth.AuthActivity
import com.narae.fliwith.src.main.MainActivity

private const val TAG = "싸피"

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setFullScreen()
        delaySplash(1500)
    }

    private fun delaySplash(delayMillis: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, getDestination()))
            finish()
        }, delayMillis)
    }

    private fun getDestination(): Class<out Activity> {
        val expireTime = sharedPreferences.getRefreshTokenExpirationTime()

        // 토큰이 만료되었다면 재로그인
        return if (expireTime <= System.currentTimeMillis()) {
            Log.d(TAG, "자동 로그인 실패")
            sharedPreferences.removeTokenData()
            AuthActivity::class.java
        }
        // 회원 정보가 존재하고 리프레쉬 토큰이 살아있으면 자동 로그인
        else {
            Log.d(TAG, "자동 로그인 성공")
            MainActivity::class.java
        }
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
