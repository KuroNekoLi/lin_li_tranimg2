package com.example.lin_li_tranimg

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lin_li_tranimg.domain.LoginRepository

class LoginViewModelFactory(private val context: Context, private val loginRepository: LoginRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(context,loginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}