package com.narae.fliwith.src.main

import android.os.Bundle
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseActivity
import com.narae.fliwith.databinding.ActivityMainBinding
import com.narae.fliwith.src.main.home.HomeFragment
import com.narae.fliwith.src.main.map.MapFragment
import com.narae.fliwith.src.main.mypage.MyPageFragment
import com.narae.fliwith.src.main.review.ReviewFragment


class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, HomeFragment()).commitAllowingStateLoss()

        binding.mainBtmNav.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.menu_main_btm_nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, HomeFragment())
                        .commitAllowingStateLoss()
                }
                R.id.menu_main_btm_nav_review -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, ReviewFragment())
                        .commitAllowingStateLoss()
                }
                R.id.menu_main_btm_nav_map -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, MapFragment())
                        .commitAllowingStateLoss()
                }
                R.id.menu_main_btm_nav_my_page -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, MyPageFragment())
                        .commitAllowingStateLoss()
                }
            }
            true
        }

    }
}