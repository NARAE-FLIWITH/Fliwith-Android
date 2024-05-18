package com.narae.fliwith.src.auth.signup

import android.os.Bundle
import android.view.View
import com.narae.fliwith.config.BaseFragment
import com.narae.fliwith.databinding.FragmentCreateAccountBinding

class CreateAccountFragment : BaseFragment<FragmentCreateAccountBinding>(FragmentCreateAccountBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        1. 이메일 입력하면 실시간 형식 체크
        2. 비밀번호 입력하면 형식에 맞는지 실시간 체크(최소 8자, 영문 숫자 포함)
        3. 모두 유효하면 버튼 활성화(실시간)
        4. 다음 눌렀을때 이메일 중복이면 오류 표시
         */
    }

}