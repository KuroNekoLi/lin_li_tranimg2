package com.example.lin_li_tranimg

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cmoney.backend2.identityprovider.service.IdentityProviderWeb
import com.example.lin_li_tranimg.domain.LoginRepository

class LoginViewModelFactory(
    private val context: Context,
    private val loginRepository: LoginRepository,
    private val identityProviderWeb: IdentityProviderWeb
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(
                context,
                loginRepository,
                identityProviderWeb
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}