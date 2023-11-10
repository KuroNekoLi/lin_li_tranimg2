package com.example.lin_li_tranimg.presentation

data class LoginScreenState (
    var accountText: String = "",
    var passwordText: String = "",
    var isEyeOpened: Boolean = true,
    var isRememberSwitchBarOn: Boolean = false,
    var isLoginButtonEnabled: Boolean = false
)