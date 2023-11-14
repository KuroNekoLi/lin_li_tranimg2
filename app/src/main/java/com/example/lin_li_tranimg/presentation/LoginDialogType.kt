package com.example.lin_li_tranimg.presentation

/**
 * 按下登入後會顯示的對話框類型
 */
sealed class LoginDialogType {
    /**
     * 不顯示對話框
     */
    object None : LoginDialogType()

    /**
     * 顯示登入錯誤的對話框
     * @param message 錯誤訊息
     */
    data class Error(val message: String) : LoginDialogType()

    /**
     * 顯示登入成功的對話框
     */
    object Success : LoginDialogType()

    /**
     * 顯示正在登入的對話框
     */
    object Loading : LoginDialogType()
}
