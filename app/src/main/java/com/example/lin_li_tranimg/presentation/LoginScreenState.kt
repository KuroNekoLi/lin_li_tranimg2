package com.example.lin_li_tranimg.presentation

data class LoginScreenState(
    var accountText: String = "",
    var passwordText: String = "",
    var isEyeOpened: Boolean = true,
    var isRememberSwitchBarOn: Boolean = false,
    var errorText: String = "",
    var isLoginButtonEnabled: Boolean = false
)