package com.example.lin_li_tranimg.presentation.compose

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
 * 訪客頁面
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestScreen(
    title: String,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = title,
                onNavigationClick = onBackClick
            )
        }
    ) {
        Text(text = title)
    }
}