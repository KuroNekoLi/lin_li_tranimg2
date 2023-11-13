package com.example.lin_li_tranimg.presentation

data class LoginScreenState(
    val accountText: String = "",
    val passwordText: String = "",
    val isEyeOpened: Boolean = true,
    val isRememberSwitchBarOn: Boolean = false,
    val isError: String?,
    val isLoginButtonEnabled: Boolean = false,
    val isSuccess: Boolean?,
    val isLoading: Boolean?
)