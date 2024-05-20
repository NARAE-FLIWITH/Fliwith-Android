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

enum class DESTINATION {
    HOME, REVIEW, MAP, MYPAGE, RECOMMEND, RECOMMEND_DETAIL, RECOMMEND_SEARCH, REVIEW_DETAIL, REVIEW_WRITE
}

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 네비게이션 호스트
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment

        // 네비게이션 컨트롤러
        val navController = navHostFragment.navController

        // 바인딩
        NavigationUI.setupWithNavController(binding.mainBtmNav, navController)
    }

}