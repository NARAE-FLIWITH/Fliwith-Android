package com.narae.fliwith.src.main

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseActivity
import com.narae.fliwith.databinding.ActivityMainBinding
import com.narae.fliwith.src.auth.AuthActivity
import com.narae.fliwith.util.showCustomSnackBar

enum class DISABILITY {
    HEARING, VISUAL, PHYSICAL, NONDISABLED, NONE, NONSELECTED;

    companion object {
        fun fromString(value: String): DISABILITY {
            return when (value) {
                "시각장애" -> VISUAL
                "청각장애" -> HEARING
                "지체장애" -> PHYSICAL
                "비장애" -> NONDISABLED
                "선택 안 함" -> NONE
                else -> NONSELECTED // 기본 값을 설정
            }
        }

        fun fromEnum(value: DISABILITY): String {
            return when (value) {
                VISUAL -> "시각장애"
                HEARING -> "청각장애"
                PHYSICAL -> "지체장애"
                NONDISABLED -> "비장애"
                NONE -> "선택 안 함"
                else -> "아무것도 없음"
            }
        }
    }
}

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private var lastBackPressedTime = 0L
    private val loginViewModel by viewModels<LoginViewModel>()

    // 더블탭 뒤로 가기
    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (lastBackPressedTime > System.currentTimeMillis() - 2000) {
                    finish()
                } else {
                    showCustomSnackBar(
                        this@MainActivity,
                        binding.root,
                        "앱을 종료하려면 뒤로 가기를 한 번 더 눌러주세요"
                    )
                    lastBackPressedTime = System.currentTimeMillis()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 두번 탭하여 종료
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // 네비게이션 호스트
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment

        // 네비게이션 컨트롤러
        val navController = navHostFragment.navController

        if (Intent.ACTION_VIEW == intent.action) {
            intent?.data?.let { uri ->
                val reviewId = uri.getQueryParameter("reviewId")
                val bundle = Bundle()
                if (reviewId != null) {
                    bundle.putInt("reviewId", reviewId.toInt())
                }

                // 먼저 Navigation 수행
                navController.navigate(R.id.action_global_reviewDetailFragment, bundle)

                // 마지막으로 리뷰 탭을 프로그래매틱하게 선택
                binding.mainBtmNav.selectedItemId = R.id.menu_main_btm_nav_review
            }
        }

        // 바인딩
        NavigationUI.setupWithNavController(binding.mainBtmNav, navController)
        binding.mainBtmNav.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item, navController)
            return@setOnItemSelectedListener true
        }

        observeLogout()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.dispatchTouchEvent(ev)
    }

    private fun observeLogout() {
        loginViewModel.loginStatus.observe(this) {
            if (!it) {
                val intent = Intent(this, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    fun viewGone() {
        binding.mainBtmNav.visibility = View.GONE
    }

    fun viewVisible() {
        binding.mainBtmNav.visibility = View.VISIBLE
    }
}
