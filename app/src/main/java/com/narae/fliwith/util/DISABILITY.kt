package com.narae.fliwith.util

import com.narae.fliwith.util.DISABILITY.*

enum class DISABILITY {
    HEARING, VISUAL, PHYSICAL, NONDISABLED, NONE, NOTSELECTED;

    companion object {
        fun fromString(value: String): DISABILITY {
            return when (value) {
                "시각장애" -> VISUAL
                "청각장애" -> HEARING
                "지체장애" -> PHYSICAL
                "비장애" -> NONDISABLED
                "선택 안 함" -> NONE
                else -> NOTSELECTED // 기본 값을 설정
            }
        }

        fun fromEnum(value: DISABILITY) : String {
            return when (value) {
                VISUAL -> "시각장애"
                HEARING -> "청각장애"
                PHYSICAL -> "지체장애"
                NONDISABLED -> "비장애"
                NONE -> "선택 안 함"
                else -> "선택 X" // 기본 값
            }
        }
    }
}  