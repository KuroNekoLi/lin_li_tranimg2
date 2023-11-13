package com.example.lin_li_tranimg

import androidx.navigation.NavHostController
import com.example.lin_li_tranimg.AppScreens.FORGET_PASSWORD_SCREEN
import com.example.lin_li_tranimg.AppScreens.GUEST_SCREEN
import com.example.lin_li_tranimg.AppScreens.HOME_SCREEN
import com.example.lin_li_tranimg.AppScreens.LOGIN_SCREEN
import com.example.lin_li_tranimg.AppScreens.REGISTER_SCREEN
import com.example.lin_li_tranimg.AppScreens.STOCK_SCREEN

/**
 * Screens used in [AppDestinations]
 */
private object AppScreens {
    const val HOME_SCREEN = "home_screen"
    const val LOGIN_SCREEN = "login_screen"
    const val REGISTER_SCREEN = "register_screen"
    const val GUEST_SCREEN = "guest_screen"
    const val FORGET_PASSWORD_SCREEN = "forget_password_screen"
    const val STOCK_SCREEN = "stock_screen"
}

/**
 * Destinations used in the [MainActivity]
 */
object AppDestinations {
    const val HOME_ROUTE = HOME_SCREEN
    const val LOGIN_ROUTE = LOGIN_SCREEN
    const val REGISTER_ROUTE = REGISTER_SCREEN
    const val GUEST_ROUTE = GUEST_SCREEN
    const val FORGET_PASSWORD_ROUTE = FORGET_PASSWORD_SCREEN
    const val STOCK_ROUTE = STOCK_SCREEN
}

/**
 * Models the navigation actions in the app.
 */
class AppNavigationActions(private val navController: NavHostController) {
    fun navigateToLogin() {
        navController.navigate(LOGIN_SCREEN)
    }

    fun navigateToRegister() {
        navController.navigate(REGISTER_SCREEN)
    }

    fun navigateToGuest() {
        navController.navigate(GUEST_SCREEN)
    }

    fun navigateToForgetPassWord() {
        navController.navigate(FORGET_PASSWORD_SCREEN)
    }

    fun navigateToStock() {
        navController.navigate(STOCK_SCREEN)
    }

    fun popBackStack() {
        navController.popBackStack()
    }
}