package com.example.lin_li_tranimg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.lin_li_tranimg.presentation.compose.AppNavHost
import com.example.lin_li_tranimg.ui.theme.Lin_li_tranimgTheme
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {
    // Lazy inject LoginViewModel
    private val loginViewModel: LoginViewModel by viewModel()

    companion object {
        const val TAG = "LinLi"
        const val LOGIN_SCREEN = "login_screen"
        const val REGISTER_SCREEN = "register_screen"
        const val GUEST_SCREEN = "guest_screen"
        const val FORGET_PASSWORD_SCREEN = "forget_password_screen"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Lin_li_tranimgTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost(viewModel = loginViewModel)
                }
            }
        }
    }
}
