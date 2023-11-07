package com.example.lin_li_tranimg
import PreferencesManager
import android.content.Context
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class LoginViewModel(private val context: Context) : ViewModel() {
    // 創建PreferencesManager的實例
    private val preferencesManager = PreferencesManager(context)
    private val _accountState = MutableStateFlow(TextFieldValue(""))
    private val _passwordState = MutableStateFlow(TextFieldValue(""))
    val accountState: StateFlow<TextFieldValue> = _accountState
    val passwordState: StateFlow<TextFieldValue> = _passwordState
    // 使用PreferencesManager追蹤可見狀態
    private val _isAccountVisible = MutableStateFlow(true)
    val isAccountVisible: StateFlow<Boolean> = _isAccountVisible.asStateFlow()

    init {
        //從DataStore讀取帳號的可見狀態並更新_isPasswordVisible
        viewModelScope.launch {
            preferencesManager.isPasswordVisible.collect { isVisible ->
                _isAccountVisible.value = isVisible
            }
        }
    }

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

    // 當切換帳號可見狀態時，更新狀態並儲存到DataStore
    fun onTogglePasswordVisibility() {
        viewModelScope.launch {
            val newVisibility = !_isAccountVisible.value
            _isAccountVisible.value = newVisibility
            // 更新DataStore中的可見狀態
            preferencesManager.updateAccountVisibility(newVisibility)
        }
    }
}