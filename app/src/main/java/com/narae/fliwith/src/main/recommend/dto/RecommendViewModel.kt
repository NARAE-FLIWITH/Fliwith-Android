package com.narae.fliwith.src.main.recommend.dto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecommendViewModel : ViewModel() {
    private val _selectedRegionButtonText = MutableLiveData<String>()
    private val _selectedTypeButtonText = MutableLiveData<String>()
    private val _selectedDisableButtonText = MutableLiveData<String>()
    private val _selectedMemberButtonText = MutableLiveData<String>()
    private val _selectedDate = MutableLiveData<String>()
    val selectedRegionButtonText: LiveData<String> get() = _selectedRegionButtonText
    val selectedTypeButtonText: LiveData<String> get() = _selectedTypeButtonText
    val selectedDisableButtonText: LiveData<String> get() = _selectedDisableButtonText
    val selectedMemberButtonText: LiveData<String> get() = _selectedMemberButtonText
    val selectDate: LiveData<String> get() = _selectedDate
    fun setSelectedRegionButtonText(text: String) {
        _selectedRegionButtonText.value = text
    }

    fun setSelectedTypeButtonText(text: String) {
        _selectedTypeButtonText.value = text
    }

    fun setSelectedMemberButtonText(text: String) {
        _selectedMemberButtonText.value = text
    }

    // 장애 선택
    fun setSelectedDisableButtonText(text: String) {
        _selectedDisableButtonText.value = text
    }

    fun setSelectDate(text: String) {
        _selectedDate.value = text
    }

    fun getPeopleNum(text: String): Int {
        return when (text) {
            "1인" -> 1
            "2인" -> 2
            "3인" -> 3
            "4인 이상" -> 4
            else -> 0 // 기본 값 (알 수 없는 경우)
        }
    }
}