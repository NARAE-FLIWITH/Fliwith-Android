package com.narae.fliwith.util

enum class DISABILITY {
    HEARING, VISUAL, PHYSICAL, NONDISABLED, NONE;

    companion object {
        fun fromString(value: String): DISABILITY {
            return when (value) {
                "시각장애" -> DISABILITY.VISUAL
                "청각장애" -> DISABILITY.HEARING
                "지체장애" -> DISABILITY.PHYSICAL
                "비장애" -> DISABILITY.NONDISABLED
                else -> DISABILITY.NONE
            }
        }

        fun fromEnum(value: DISABILITY) : String {
            return when (value) {
                DISABILITY.VISUAL -> "시각장애"
                DISABILITY.HEARING -> "청각장애"
                DISABILITY.PHYSICAL -> "지체장애"
                DISABILITY.NONDISABLED -> "비장애"
                DISABILITY.NONE -> "선택 안 함"
            }
        }
    }
}  