package com.narae.fliwith.src.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseActivity
import com.narae.fliwith.databinding.ActivityMainBinding
import com.narae.fliwith.src.main.home.HomeFragment
import com.narae.fliwith.src.main.map.MapFragment
import com.narae.fliwith.src.main.mypage.MyPageFragment
import com.narae.fliwith.src.main.recommend.RecommendDetailFragment
import com.narae.fliwith.src.main.recommend.RecommendFragment
import com.narae.fliwith.src.main.recommend.RecommendSearchFragment
import com.narae.fliwith.src.main.review.ReviewDetailFragment
import com.narae.fliwith.src.main.review.ReviewFragment
import com.narae.fliwith.src.main.review.ReviewWriteFragment

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

        fun fromEnum(value:DISABILITY) : String {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
