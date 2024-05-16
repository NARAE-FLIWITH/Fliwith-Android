package com.narae.fliwith.src.main.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecommendViewModel : ViewModel() {
    private val _selectedRegionButtonText = MutableLiveData<String>()
    private val _selectedTypeButtonText = MutableLiveData<String>()
    private val _selectedDisableButtonText = MutableLiveData<String>()
    private val _selectedMemberButtonText = MutableLiveData<String>()
    val selectedRegionButtonText: LiveData<String> get() = _selectedRegionButtonText
    val selectedTypeButtonText: LiveData<String> get() = _selectedTypeButtonText
    val selectedDisableButtonText: LiveData<String> get() = _selectedDisableButtonText
    val selectedMemberButtonText: LiveData<String> get() = _selectedMemberButtonText
    fun setSelectedRegionButtonText(text: String) {
        _selectedRegionButtonText.value = text
    }

    fun setSelectedTypeButtonText(text: String) {
        _selectedTypeButtonText.value = text
    }

    fun setSelectedMemberButtonText(text: String) {
        _selectedMemberButtonText.value = text
    }

    fun setSelectedDisableButtonText(text: String) {
        _selectedDisableButtonText.value = text
    }
}