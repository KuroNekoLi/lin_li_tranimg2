package com.example.lin_li_tranimg
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class LoginViewModel : ViewModel() {
    private val _accountState = MutableStateFlow(TextFieldValue(""))
    private val _passwordState = MutableStateFlow(TextFieldValue(""))
    val accountState: StateFlow<TextFieldValue> = _accountState
    val passwordState: StateFlow<TextFieldValue> = _passwordState

    fun onTextAccountChange(newText: TextFieldValue) {
        viewModelScope.launch {
            _accountState.value = newText
        }
    }

    fun onTextPasswordChange(newText: TextFieldValue) {
        viewModelScope.launch {
            _passwordState.value = newText
        }
    }
}