package com.narae.fliwith.src.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.narae.fliwith.config.ApplicationClass.Companion.sharedPreferences
import com.narae.fliwith.config.BaseActivity
import com.narae.fliwith.databinding.ActivitySplashBinding
import com.narae.fliwith.src.auth.AuthActivity
import com.narae.fliwith.src.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "SplashActivity"

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private lateinit var splashScreen: SplashScreen
    override fun preload() {
        super.preload()
        splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }

        val destinationActivity = getDestination()

        val intent = Intent(this@SplashActivity, destinationActivity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun getDestination(): Class<out Activity> {
        val tokenData = sharedPreferences.getTokenData()

        // 리프레쉬 토큰이 만료되었거나 없으면 로그인
        return if (tokenData.refreshTokenExpirationTime <= System.currentTimeMillis()) {
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

    override fun onDestroy() {
        splashScreen.setKeepOnScreenCondition { false }
        super.onDestroy()
    }
}
