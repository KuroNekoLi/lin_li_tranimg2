package com.example.lin_li_tranimg
import PreferencesManager
import android.content.Context
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
    // 新增的状态变量用于跟踪Switch的状态和密码
    private val _rememberPasswordSwitchState = MutableStateFlow(false)
    val rememberPasswordSwitchState: StateFlow<Boolean> = _rememberPasswordSwitchState.asStateFlow()

    init {
        //從DataStore讀取帳號的可見狀態並更新_isPasswordVisible
        viewModelScope.launch {
            preferencesManager.isPasswordVisible.collect { isVisible ->
                _isAccountVisible.value = isVisible
            }
            preferencesManager.passwordFlow.collect { savedPassword ->
                if (!savedPassword.isNullOrEmpty()) {
                    _passwordState.value = TextFieldValue(savedPassword)
                    _rememberPasswordSwitchState.value = true
                }
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


    // 记住或清除密码的方法
    fun onRememberPasswordSwitchChanged(remember: Boolean) {
        viewModelScope.launch {
            _rememberPasswordSwitchState.value = remember
            if (remember) {
                preferencesManager.savePassword(_passwordState.value.text)
            } else {
                preferencesManager.clearPassword()
            }
        }
    }

    // 检查字符串是否为电子邮箱格式
    private fun isValidEmail(email: TextFieldValue): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()
    }

    // 检查字符串是否为有效的手机号码
    private fun isValidPhone(phone: TextFieldValue): Boolean {
        return phone.text.matches("^09\\d{8}$".toRegex())
    }

    // 检查密码长度是否大于等于4
    private fun isValidPassword(password: TextFieldValue): Boolean {
        return password.text.length >= 4
    }


    val isLoginButtonEnabled: StateFlow<Boolean> = combine(
        _accountState,
        _passwordState
    ) { account, password ->
        // 检查账号是否为有效的电子邮箱或手机号码，并且密码长度是否大于等于4
        val isAccountValid = isValidEmail(account) || isValidPhone(account)
        val isPasswordValid = isValidPassword(password)
        isAccountValid && isPasswordValid
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

}