package com.example.lin_li_tranimg.presentation

/**
 * 用戶登入事件
 */
sealed class LoginEvent {
    /**
     * 用戶輸入帳號時觸發的事件。
     * @param account 用戶輸入的帳號。
     */
    data class AccountTextEntered(val account: String) : LoginEvent()

    /**
     * 用戶輸入密碼時觸發的事件。
     * @param password 用戶輸入的密碼。
     */
    data class PasswordTextEntered(val password: String) : LoginEvent()
    /**
     * 用戶點擊顯示/隱藏帳號圖示時觸發的事件。
     */
    object IconEyeClicked : LoginEvent()
    /**
     * 用戶切換記住帳密開關時觸發的事件。
     */
    object RememberBarSwitched : LoginEvent()
    /**
     * 用戶點擊登入按鈕時觸發的事件。
     */
    object LoginButtonClicked : LoginEvent()
}