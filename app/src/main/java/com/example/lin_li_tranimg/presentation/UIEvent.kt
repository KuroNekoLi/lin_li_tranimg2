package com.example.lin_li_tranimg.presentation

sealed class UIEvent {
    object ShowLoadingDialog : UIEvent()

    object ShowSuccessDialog : UIEvent()
    class ShowErrorDialog(val error: String) : UIEvent()
    object NavigateToHomeScreen : UIEvent()
    object NavigateToForgetPasswordScreen : UIEvent()
    object NavigateToGuestScreen : UIEvent()
    object NavigateToRegisterScreen : UIEvent()
}