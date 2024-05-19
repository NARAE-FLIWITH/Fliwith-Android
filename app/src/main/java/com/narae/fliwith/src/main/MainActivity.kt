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

//        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, HomeFragment()).commitAllowingStateLoss()
//
//        binding.mainBtmNav.setOnItemSelectedListener{ item ->
//            when (item.itemId) {
//                R.id.menu_main_btm_nav_home -> {
//                    changeFragmentView(DESTINATION.HOME)
//                }
//                R.id.menu_main_btm_nav_review -> {
//                    changeFragmentView(DESTINATION.REVIEW)
//                }
//                R.id.menu_main_btm_nav_map -> {
//                    changeFragmentView(DESTINATION.MAP)
//                }
//                R.id.menu_main_btm_nav_my_page -> {
//                    changeFragmentView(DESTINATION.MYPAGE)
//                }
//            }
//            true
//        }

    }
//    fun changeFragmentView(destination: DESTINATION, dest: String? = null) {
//        val transaction = supportFragmentManager.beginTransaction()
//        when (destination) {
//            DESTINATION.HOME -> {
//                val fragment = HomeFragment.newInstance("", "")
//                transaction.replace(binding.mainFragment.id, fragment)
//                viewVisible()
//            }
//
//            DESTINATION.REVIEW -> {
//                val fragment = ReviewFragment.newInstance("", "")
//                transaction.replace(binding.mainFragment.id, fragment)
//                viewVisible()
//            }
//
//            DESTINATION.MAP -> {
//                val fragment = MapFragment.newInstance("", "")
//                transaction.replace(binding.mainFragment.id, fragment)
//                viewVisible()
//            }
//
//            DESTINATION.MYPAGE -> {
//                val fragment = MyPageFragment.newInstance("", "")
//                transaction.replace(binding.mainFragment.id, fragment)
//                viewVisible()
//            }
//
//            DESTINATION.RECOMMEND -> {
//                val fragment = RecommendFragment.newInstance("", "")
//                transaction.replace(binding.mainFragment.id, fragment)
//                viewGone()
//            }
//
//            DESTINATION.RECOMMEND_DETAIL -> {
//                val fragment = RecommendDetailFragment.newInstance(dest ?: "", "")
//                transaction.replace(binding.mainFragment.id, fragment)
//                viewGone()
//            }
//
//            DESTINATION.RECOMMEND_SEARCH -> {
//                val fragment = RecommendSearchFragment.newInstance(dest ?: "", "")
//                transaction.replace(binding.mainFragment.id, fragment)
//                viewGone()
//            }
//
//            DESTINATION.REVIEW_DETAIL -> {
//                val fragment = ReviewDetailFragment.newInstance(dest ?: "", "")
//                transaction.replace(binding.mainFragment.id, fragment)
//                viewGone()
//            }
//
//            DESTINATION.REVIEW_WRITE -> {
//                val fragment = ReviewWriteFragment.newInstance(dest ?: "", "")
//                transaction.replace(binding.mainFragment.id, fragment)
//                viewGone()
//            }
//        }
//        transaction.commitAllowingStateLoss()
//    }
//
//    fun viewGone() {
//        binding.mainBtmNav.visibility = View.GONE
//    }
//
//    fun viewVisible() {
//        binding.mainBtmNav.visibility = View.VISIBLE
//    }

}