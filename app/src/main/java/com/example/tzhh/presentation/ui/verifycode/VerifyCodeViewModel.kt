package com.example.tzhh.ui.verifycode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tzhh.utils.SingleLiveEvent

class VerifyCodeViewModel : ViewModel() {

    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean> get() = _isButtonEnabled

    private val _code = MutableLiveData<String>()
    val code: LiveData<String> get() = _code

    private val _navigateToMainActivity = SingleLiveEvent<Unit>()
    val navigateToMainActivity: LiveData<Unit> get() = _navigateToMainActivity

    init {
        _isButtonEnabled.value = false
    }

    fun onCodeChanged(digit: String, position: Int) {
        val currentCode = _code.value?.toCharArray() ?: CharArray(4)
        currentCode[position] = digit.firstOrNull() ?: ' '
        _code.value = currentCode.joinToString("")
        _isButtonEnabled.value = _code.value?.length == 4 && _code.value?.all { it.isDigit() } == true
    }

    fun onConfirmClicked() {
        if (_isButtonEnabled.value == true) {
            _navigateToMainActivity.call()
        }
    }
}