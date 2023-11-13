package com.example.lin_li_tranimg.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmoney.backend2.base.model.exception.ServerException
import com.example.lin_li_tranimg.domain.LoginRepository
import com.example.lin_li_tranimg.presentation.LoginEvent
import com.example.lin_li_tranimg.presentation.LoginScreenState
import kotlinx.coroutines.Dispatchers
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
            isError = null,
            isSuccess = null,
            isLoading = null
        )
    )

    val loginScreenState: State<LoginScreenState> = _loginScreenState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.savedPasswordFlow().collect { savedPassword ->
                if (!savedPassword.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        _loginScreenState.value =
                            _loginScreenState.value.copy(passwordText = savedPassword)
                        updateLoginButtonState()
                    }
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.savedAccountFlow().collect { savedAccount ->
                if (!savedAccount.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        _loginScreenState.value =
                            _loginScreenState.value.copy(accountText = savedAccount)
                        updateLoginButtonState()
                    }
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.isAccountVisibleFlow().collect { isVisible ->
                withContext(Dispatchers.Main) {
                    _loginScreenState.value = _loginScreenState.value.copy(isEyeOpened = isVisible)
                }
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

    fun login() {
        val account = _loginScreenState.value.accountText
        val password = _loginScreenState.value.passwordText
        viewModelScope.launch {
            _loginScreenState.value = loginScreenState.value.copy(
                isLoading = true
            )
            val result = loginRepository.loginByEmail(account, password)

            if (result.isSuccess) {
                _loginScreenState.value = loginScreenState.value.copy(
                    isLoading = null,
                    isSuccess = true
                )
            }

            if (result.isFailure) {
                val errorMessage =
                    (result.exceptionOrNull() as? ServerException)?.message ?: "登入失敗"
                _loginScreenState.value = loginScreenState.value.copy(
                    isLoading = null,
                    isSuccess = null,
                    isError = errorMessage
                )
            }
        }
    }


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
}

