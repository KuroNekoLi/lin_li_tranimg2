package com.example.lin_li_tranimg.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmoney.backend2.identityprovider.service.IdentityProviderWeb
import com.cmoney.backend2.identityprovider.service.api.gettoken.GetTokenResponseBody
import com.example.lin_li_tranimg.domain.LoginRepository
import com.example.lin_li_tranimg.presentation.LoginEvent
import com.example.lin_li_tranimg.presentation.LoginScreenState
import com.example.lin_li_tranimg.presentation.UIEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val identityProviderWeb: IdentityProviderWeb
) : ViewModel() {

    private val _loginScreenState = mutableStateOf(
        LoginScreenState(
            accountText = "",
            passwordText = "",
            isEyeOpened = true,
            isRememberSwitchBarOn = false,
            isLoginButtonEnabled = false
        )
    )

    val loginScreenState: State<LoginScreenState> = _loginScreenState

    private val _uiEventFlow = MutableSharedFlow<UIEvent>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    init {
        //從DataStore讀取帳號的可見狀態並更新_isPasswordVisible
        viewModelScope.launch(Dispatchers.IO) {
            //記憶的密碼
            loginRepository.savedPasswordFlow().collect { savedPassword ->
                if (!savedPassword.isNullOrEmpty()) {
                    _loginScreenState.value.passwordText = savedPassword
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            //記憶的帳號
            loginRepository.savedAccountFlow().collect { savedAccount ->
                if (!savedAccount.isNullOrEmpty()) {
                    _loginScreenState.value.accountText = savedAccount
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            //眼睛可不可見
            loginRepository.isAccountVisibleFlow().collect { isVisible ->
                _loginScreenState.value.isEyeOpened = isVisible
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

    fun login(account: String, password: String) {
        val md5edPassword = try {
            password.md5().orEmpty()
        } catch (e: Exception) {
            ""
        }
        if (account.isEmpty() || md5edPassword.isEmpty()) {
            return
        }
        viewModelScope.launch {
            _uiEventFlow.emit(
                UIEvent.ShowLoadingDialog
            )

            val result: Result<GetTokenResponseBody> = identityProviderWeb.loginByEmail(
                account = account,
                hashedPassword = md5edPassword,
            )

            if (result.isSuccess) {
                _uiEventFlow.emit(
                    UIEvent.ShowSuccessDialog
                )

                _uiEventFlow.emit(
                    UIEvent.NavigateToHomeScreen
                )
            }

            if (result.isFailure) {
                _uiEventFlow.emit(
                    UIEvent.ShowErrorDialog("登入失敗")
                )
            }
        }
    }

    @Throws(NoSuchAlgorithmException::class)
    private fun String.md5(): String? {
        val md5: MessageDigest = MessageDigest.getInstance("MD5")
        val digest: ByteArray = md5.digest(this.toByteArray(StandardCharsets.UTF_8))
        return java.lang.String.format("%032x", BigInteger(1, digest))
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
                    // 更新DataStore中的可見狀態
                    loginRepository.updateAccountVisibility(_loginScreenState.value.isEyeOpened)
                }
            }

            is LoginEvent.RememberBarSwitched -> {
                _loginScreenState.value = loginScreenState.value.copy(
                    isRememberSwitchBarOn = !loginScreenState.value.isRememberSwitchBarOn
                )
            }

            is LoginEvent.ForgetPassWordClicked -> {
                viewModelScope.launch {
                    _uiEventFlow.emit(
                        UIEvent.NavigateToForgetPasswordScreen
                    )
                }
            }

            is LoginEvent.GuestClicked -> {
                viewModelScope.launch {
                    _uiEventFlow.emit(
                        UIEvent.NavigateToGuestScreen
                    )
                }
            }

            is LoginEvent.RegisterClicked -> {
                viewModelScope.launch {
                    _uiEventFlow.emit(
                        UIEvent.NavigateToRegisterScreen
                    )
                }
            }

            is LoginEvent.LoginButtonClicked -> {
                viewModelScope.launch {
                    //只有在記住密碼為on時將資料儲存，否則清掉
                    if (loginScreenState.value.isRememberSwitchBarOn) {

                        loginRepository.saveUserData(
                            loginScreenState.value.accountText,
                            loginScreenState.value.passwordText
                        )
                    } else {
                        loginRepository.clearUserData()
                    }
                    login(loginScreenState.value.accountText, loginScreenState.value.passwordText)
                }
            }
        }
    }
}

