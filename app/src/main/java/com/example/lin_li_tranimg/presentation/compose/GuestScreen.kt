package com.example.lin_li_tranimg.presentation.compose

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestScreen(string: String, navController: NavController) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = string,
                onNavigationClick = { navController.navigateUp() }
            )
        }
    ) {
        Text(text = string)
    }
}