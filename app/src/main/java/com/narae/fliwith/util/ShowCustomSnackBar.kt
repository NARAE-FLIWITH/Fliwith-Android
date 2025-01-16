package com.narae.fliwith.util

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.narae.fliwith.databinding.SnackbarCustomBinding

fun showCustomSnackBar(context : Context, view: View ,msg: String) {
    //스낵바 만들기
    val snack = Snackbar.make(view, "커스텀 스낵바", Snackbar.LENGTH_LONG)

    val snackBinding =
        SnackbarCustomBinding.inflate(LayoutInflater.from(context)).apply {
            tvSearch.text = msg
        }

    //커스텀할 뷰 만들기
    val snackView = snackBinding.root

    //스낵바 객체에 내재된 레이아웃 추출
    val snackbarLayout = snack.view as Snackbar.SnackbarLayout
    snackbarLayout.setBackgroundColor(Color.TRANSPARENT)

    val layoutParams = snackbarLayout.layoutParams
    if (layoutParams is FrameLayout.LayoutParams) {
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        layoutParams.bottomMargin = convertDPtoPX(context, 120)
        snackbarLayout.layoutParams = layoutParams
    }

    //뷰 추가하기
    snackbarLayout.addView(snackView)

    //스낵바에 있는 텍스트뷰를 추출해 이를 보이지 않게 처리
    //스낵바 객체에서 추출한 레이아웃에서 findViewById로 텍스트뷰 객체를 추출
    val snackText =
        snackbarLayout.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    snackText.visibility = View.INVISIBLE

    snack.show()
}