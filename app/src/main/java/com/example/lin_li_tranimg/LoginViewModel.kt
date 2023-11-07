package com.example.lin_li_tranimg
import PreferencesManager
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class LoginViewModel( context: Context) : ViewModel() {
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
        viewModelScope.launch(Dispatchers.IO) {
            //記憶的密碼
            preferencesManager.passwordFlow.collect { savedPassword ->
                if (!savedPassword.isNullOrEmpty()) {
                    _passwordState.value = TextFieldValue(savedPassword)
                    _rememberPasswordSwitchState.value = true
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            //記憶的帳號
            preferencesManager.accountFlow.collect { savedAccount ->
                if (!savedAccount.isNullOrEmpty()) {
                    _accountState.value = TextFieldValue(savedAccount)
                    _rememberPasswordSwitchState.value = true
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO){
            //眼睛可不可見
            preferencesManager.isAccountVisible.collect { isVisible ->
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

    //滑動記住密碼時
    fun onRememberPasswordSwitchChanged(remember: Boolean) {
        viewModelScope.launch {
            _rememberPasswordSwitchState.value = remember
        }
    }

    fun rememberPassWord(remember: Boolean){
        viewModelScope.launch {
            if (remember) {
                preferencesManager.saveAccount(_accountState.value.text)
                preferencesManager.savePassword(_passwordState.value.text)
            } else {
                preferencesManager.clearAccount()
                preferencesManager.clearPassword()
            }
        }
    }
    private fun isValidEmail(email: TextFieldValue): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()
    }

    private fun isValidPhone(phone: TextFieldValue): Boolean {
        return phone.text.matches("^09\\d{8}$".toRegex())
    }

    private fun isValidPassword(password: TextFieldValue): Boolean {
        return password.text.length >= 4
    }


    val isLoginButtonEnabled: StateFlow<Boolean> = combine(
        _accountState,
        _passwordState
    ) { account, password ->
        val isAccountValid = isValidEmail(account) || isValidPhone(account)
        val isPasswordValid = isValidPassword(password)
        isAccountValid && isPasswordValid
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )
}