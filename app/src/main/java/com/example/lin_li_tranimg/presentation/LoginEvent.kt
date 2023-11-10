package com.example.lin_li_tranimg.presentation

sealed class LoginEvent {
    data class AccountTextEntered(val account: String): LoginEvent()
    data class PasswordTextEntered(val password: String): LoginEvent()
    object IconEyeClicked: LoginEvent()
    object RememberBarSwitched: LoginEvent()
    object ForgetPassWordClicked: LoginEvent()
    object GuestClicked: LoginEvent()
    object RegisterClicked: LoginEvent()
    object LoginButtonClicked: LoginEvent()
}