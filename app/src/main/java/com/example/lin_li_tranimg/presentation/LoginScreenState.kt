package com.example.lin_li_tranimg.presentation

/**
 * 登錄畫面的狀態管理類
 *
 * @property accountText 帳號輸入欄位的文字內容
 * @property passwordText 密碼輸入欄位的文字內容
 * @property isEyeOpened 密碼顯示狀態，true 表示顯示帳號，false 表示隱藏帳號
 * @property isRememberSwitchBarOn 記住密碼開關的狀態，true 表示開啟記住密碼，false 表示關閉
 * @property loginDialogType 登錄對話框類型，用於定義登錄過程中顯示的不同對話框
 * @property isLoginButtonEnabled 登錄按鈕是否可用，true 表示按鈕可點擊，false 表示按鈕不可點擊
 */
data class LoginScreenState(
    val accountText: String = "",
    val passwordText: String = "",
    val isEyeOpened: Boolean = true,
    val isRememberSwitchBarOn: Boolean = false,
    val loginDialogType: LoginDialogType,
    val isLoginButtonEnabled: Boolean = false,
)