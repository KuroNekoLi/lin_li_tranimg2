package com.example.lin_li_tranimg.ui.theme

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * 登入按鈕的樣式
 */
object ButtonStyles {
    @Composable
    fun defaultButtonColors() = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.Black,
        disabledContainerColor = DialogBackgroundColor,
        disabledContentColor = DialogTextColor
    )
}
