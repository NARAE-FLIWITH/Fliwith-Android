package com.narae.fliwith.src.main.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecommendViewModel : ViewModel() {
    private val _selectedRegionButtonText = MutableLiveData<String>()
    private val _selectedTypeButtonText = MutableLiveData<String>()
    val selectedRegionButtonText: LiveData<String> get() = _selectedRegionButtonText
    val selectedTypeButtonText: LiveData<String> get() = _selectedTypeButtonText
    fun setSelectedRegionButtonText(text: String) {
        _selectedRegionButtonText.value = text
    }

    fun setSelectedTypeButtonText(text: String) {
        _selectedTypeButtonText.value = text
    }
}