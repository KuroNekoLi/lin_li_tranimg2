package com.example.lin_li_tranimg

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lin_li_tranimg.presentation.compose.ForgetPasswordScreen
import com.example.lin_li_tranimg.presentation.compose.GuestScreen
import com.example.lin_li_tranimg.presentation.compose.HomeScreen
import com.example.lin_li_tranimg.presentation.compose.LoginScreen
import com.example.lin_li_tranimg.presentation.compose.RegisterScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
//    startDestination: String = AppDestinations.HOME_ROUTE,
    startDestination: String = AppDestinations.LOGIN_ROUTE,
    navActions: AppNavigationActions = remember(navController) {
        AppNavigationActions(navController)
    }
) {
    val popBackStack = { navActions.popBackStack() }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(AppDestinations.HOME_ROUTE) {
            HomeScreen(
                onLogin = { navActions.navigateToLogin() },
                onRegister = { navActions.navigateToRegister() }
            )
        }
        composable(AppDestinations.LOGIN_ROUTE) {
            LoginScreen(
                onLogin = { navActions.navigateToStock() },
                onRegister = { navActions.navigateToRegister() },
                onForgetPassword = { navActions.navigateToForgetPassWord() },
                onGuest = { navActions.navigateToGuest() }
            )
        }
        composable(AppDestinations.REGISTER_ROUTE) {
            RegisterScreen(
                title = "註冊畫面",
                onBackClick = popBackStack
            )
        }
        composable(AppDestinations.GUEST_ROUTE) {
            GuestScreen(
                title = "訪客畫面",
                onBackClick = popBackStack
            )
        }
        composable(AppDestinations.FORGET_PASSWORD_ROUTE) {
            ForgetPasswordScreen(
                title = "忘記密碼畫面",
                onBackClick = popBackStack
            )
        }
        composable(AppDestinations.STOCK_ROUTE) {
            ForgetPasswordScreen(
                title = "主畫面",
                onBackClick = popBackStack
            )
        }
    }
}