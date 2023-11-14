package com.example.lin_li_tranimg.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmoney.backend2.base.model.exception.ServerException
import com.example.lin_li_tranimg.domain.LoginRepository
import com.example.lin_li_tranimg.presentation.LoginDialogType
import com.example.lin_li_tranimg.presentation.LoginEvent
import com.example.lin_li_tranimg.presentation.LoginScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _loginScreenState = mutableStateOf(
        LoginScreenState(
            accountText = "",
            passwordText = "",
            isEyeOpened = true,
            isRememberSwitchBarOn = false,
            isLoginButtonEnabled = false,
            loginDialogType = LoginDialogType.None
        )
    )

    val loginScreenState: State<LoginScreenState> = _loginScreenState

    init {
        collectFlowAndSetState(loginRepository.savedPasswordFlow()) { savedPassword ->
            if (!savedPassword.isNullOrEmpty()) {
                _loginScreenState.value =
                    _loginScreenState.value.copy(passwordText = savedPassword)
                updateLoginButtonState()
            }
        }

        collectFlowAndSetState(loginRepository.savedAccountFlow()) { savedAccount ->
            if (!savedAccount.isNullOrEmpty()) {
                _loginScreenState.value =
                    _loginScreenState.value.copy(accountText = savedAccount)
                updateLoginButtonState()
            }
        }

        collectFlowAndSetState(loginRepository.isAccountVisibleFlow()) { isVisible ->
            _loginScreenState.value = _loginScreenState.value.copy(isEyeOpened = isVisible)
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

    private fun login() {
        val account = _loginScreenState.value.accountText
        val password = _loginScreenState.value.passwordText
        viewModelScope.launch {
            _loginScreenState.value = loginScreenState.value.copy(
                loginDialogType = LoginDialogType.Loading
            )
            val loginByEmailResult = loginRepository.loginByEmail(account, password)
            val loginByCellPhoneResult = loginRepository.loginCellphone(account, password)

            if (loginByEmailResult.isSuccess || loginByCellPhoneResult.isSuccess) {
                _loginScreenState.value = loginScreenState.value.copy(
                    loginDialogType = LoginDialogType.Success
                )
            } else {
                if (loginByEmailResult.isFailure) {
                    val errorMessage =
                        (loginByEmailResult.exceptionOrNull() as? ServerException)?.message
                            ?: "登入失敗"
                    _loginScreenState.value = loginScreenState.value.copy(
                        loginDialogType = LoginDialogType.Error(errorMessage)
                    )
                }

                if (loginByCellPhoneResult.isFailure) {
                    val errorMessage =
                        (loginByCellPhoneResult.exceptionOrNull() as? ServerException)?.message
                            ?: "登入失敗"
                    _loginScreenState.value = loginScreenState.value.copy(
                        loginDialogType = LoginDialogType.Error(errorMessage)
                    )
                }
            }
        }
    }
    /**
     * 管理使用者的登入事件
     */
    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.AccountTextEntered -> {
                val isValidAccount =
                    isValidEmail(TextFieldValue(event.account)) || isValidPhone(TextFieldValue(event.account))
                val isValidPassword =
                    isValidPassword(TextFieldValue(_loginScreenState.value.passwordText))
                _loginScreenState.value = loginScreenState.value.copy(
                    accountText = event.account,
                    isLoginButtonEnabled = isValidAccount && isValidPassword
                )
            }

            is LoginEvent.PasswordTextEntered -> {
                val isValidAccount =
                    isValidEmail(TextFieldValue(_loginScreenState.value.accountText)) || isValidPhone(
                        TextFieldValue(_loginScreenState.value.accountText)
                    )
                val isValidPassword = isValidPassword(TextFieldValue(event.password))
                _loginScreenState.value = loginScreenState.value.copy(
                    passwordText = event.password,
                    isLoginButtonEnabled = isValidAccount && isValidPassword
                )
            }

            is LoginEvent.IconEyeClicked -> {
                _loginScreenState.value = loginScreenState.value.copy(
                    isEyeOpened = !loginScreenState.value.isEyeOpened
                )
                viewModelScope.launch {
                    loginRepository.updateAccountVisibility(_loginScreenState.value.isEyeOpened)
                }
            }

            is LoginEvent.RememberBarSwitched -> {
                _loginScreenState.value = loginScreenState.value.copy(
                    isRememberSwitchBarOn = !loginScreenState.value.isRememberSwitchBarOn
                )
            }

            is LoginEvent.LoginButtonClicked -> {
                viewModelScope.launch {
                    if (loginScreenState.value.isRememberSwitchBarOn) {

                        loginRepository.saveUserData(
                            loginScreenState.value.accountText,
                            loginScreenState.value.passwordText
                        )
                    } else {
                        loginRepository.clearUserData()
                    }
                    login()
                }
            }
        }
    }

    private fun updateLoginButtonState() {
        val isValidAccount = isValidEmail(TextFieldValue(_loginScreenState.value.accountText)) ||
                isValidPhone(TextFieldValue(_loginScreenState.value.accountText))
        val isValidPassword = isValidPassword(TextFieldValue(_loginScreenState.value.passwordText))
        _loginScreenState.value = _loginScreenState.value.copy(
            isLoginButtonEnabled = isValidAccount && isValidPassword,
            isRememberSwitchBarOn = true
        )
    }

    private fun <T> collectFlowAndSetState(
        flow: Flow<T>,
        setState: (T) -> Unit
    ) {
        viewModelScope.launch {
            flow.flowOn(Dispatchers.IO).collect { value ->
                withContext(Dispatchers.Main) {
                    setState(value)
                }
            }
        }
    }

    /**
     * 將對話框的狀態設為無對話框
     */
    fun resetDialogTypeToNone() {
        _loginScreenState.value = _loginScreenState.value.copy(
            loginDialogType = LoginDialogType.None
        )
    }
}

