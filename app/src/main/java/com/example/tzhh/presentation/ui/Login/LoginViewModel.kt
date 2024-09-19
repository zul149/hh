package com.example.tzhh.ui.Login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tzhh.utils.SingleLiveEvent

class LoginViewModel : ViewModel() {

    // использовать для обработки событий, которые должны произойти только один раз, таких как навигация или показ уведомлений.
    private val _navigateToNextScreen = SingleLiveEvent<Unit>()
    val navigateToNextScreen: LiveData<Unit> get() = _navigateToNextScreen

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _showClearButton = MutableLiveData<Boolean>()
    val showClearButton: LiveData<Boolean> get() = _showClearButton

    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean> get() = _isButtonEnabled

    private val _emailError = MutableLiveData<Boolean>()
    val emailError: MutableLiveData<Boolean> get() = _emailError

    init {
        // Изначально кнопка неактивна
        _isButtonEnabled.value = false
        _emailError.value = false
        _showClearButton.value = false
    }

    fun onEmailChanged(email: String) {
        _email.value = email.trim()
        _isButtonEnabled.value = email.isNotEmpty()
        _showClearButton.value = email.isNotEmpty()
        _emailError.value = false
    }

    fun onClearEmailClicked() {
        _email.value = ""
        _isButtonEnabled.value = false
        _showClearButton.value = false
        _emailError.value = false
    }

    fun onContinueClicked() {
        if (_email.value?.isValidEmail() == true) {
            _navigateToNextScreen.call()
        } else {
            _emailError.value = true
        }
    }

    private fun String.isValidEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}