package com.narae.fliwith.src.auth.signup

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.narae.fliwith.R
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentSendMailBinding
import com.narae.fliwith.databinding.SnackbarCustomBinding
import com.narae.fliwith.src.auth.AuthApi.authService
import com.narae.fliwith.src.auth.AuthViewModel
import com.narae.fliwith.util.convertDPtoPX
import com.narae.fliwith.util.setOnSingleClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "SendMailFragment"

class SendMailFragment : BaseFragment<FragmentSendMailBinding>(FragmentSendMailBinding::inflate) {
    private val viewModel by activityViewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnSingleClickListener {
            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    authService.isAuthenticatedEmail(viewModel.user.email)
                }

                if (response.isSuccessful) {
                    viewModel.removeUser()
                    navController.navigate(R.id.action_sendMailFragment_to_completeFragment)
                } else {
                    showCustomSnackBar()
                    Log.d(TAG, "Email Authentication Error : ${response.errorBody()?.string()}")
                }
            }
        }
    }

    private fun showCustomSnackBar() {
        //스낵바 만들기
        val snack = Snackbar.make(binding.root, "커스텀 스낵바", Snackbar.LENGTH_LONG)

        val snackBinding =
            SnackbarCustomBinding.inflate(layoutInflater).apply {
                tvSearch.text = "이메일 인증을 완료해 주세요."
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
            layoutParams.bottomMargin = convertDPtoPX(requireContext(), 120)
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
}