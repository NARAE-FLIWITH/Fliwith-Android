package com.narae.fliwith.src.main

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseActivity
import com.narae.fliwith.databinding.ActivityMainBinding
import com.narae.fliwith.databinding.SnackbarCustomBinding
import com.narae.fliwith.util.convertDPtoPX

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

    // 더블탭 뒤로 가기
    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (lastBackPressedTime > System.currentTimeMillis() - 2000) {
                    finish()
                } else {
                    //스낵바 만들기
                    val snack2 = Snackbar.make(binding.root, "커스텀 스낵바", Snackbar.LENGTH_LONG)

                    val snackBinding =
                        SnackbarCustomBinding.inflate(layoutInflater)

                    //커스텀할 뷰 만들기
                    val snackView = snackBinding.root

                    //스낵바 객체에 내재된 레이아웃 추출
                    val snackbarLayout = snack2.view as Snackbar.SnackbarLayout
                    snackbarLayout.setBackgroundColor(Color.TRANSPARENT)

                    val layoutParams = snackbarLayout.layoutParams
                    if (layoutParams is FrameLayout.LayoutParams) {
                        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
                        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                        layoutParams.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                        layoutParams.bottomMargin = convertDPtoPX(this@MainActivity, 90)
                        snackbarLayout.layoutParams = layoutParams
                    }

                    //뷰 추가하기
                    snackbarLayout.addView(snackView)

                    //스낵바에 있는 텍스트뷰를 추출해 이를 보이지 않게 처리
                    //스낵바 객체에서 추출한 레이아웃에서 findViewById로 텍스트뷰 객체를 추출
                    val snackText =
                        snackbarLayout.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    snackText.visibility = View.INVISIBLE

                    snack2.show()
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

        // 바인딩
        NavigationUI.setupWithNavController(binding.mainBtmNav, navController)
    }

    public fun viewGone() {
        binding.mainBtmNav.visibility = View.GONE
    }

    public fun viewVisible() {
        binding.mainBtmNav.visibility = View.VISIBLE
    }
}
