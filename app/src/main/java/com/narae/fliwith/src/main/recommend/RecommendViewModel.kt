package com.narae.fliwith.src.main.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecommendViewModel : ViewModel() {
    private val _selectedButtonText = MutableLiveData<String>()
    val selectedButtonText: LiveData<String> get() = _selectedButtonText

    fun setSelectedButtonText(text: String) {
        _selectedButtonText.value = text
    }
}