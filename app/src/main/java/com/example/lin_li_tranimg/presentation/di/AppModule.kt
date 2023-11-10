package com.example.lin_li_tranimg.presentation.di

import PreferencesManager
import com.cmoney.backend2.base.model.manager.GlobalBackend2Manager
import com.example.lin_li_tranimg.domain.LoginRepository
import com.example.lin_li_tranimg.domain.LoginRepositoryImpl
import com.example.lin_li_tranimg.presentation.viewmodel.LoginViewModel
import com.google.androidbrowserhelper.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        GlobalBackend2Manager.Builder.build(
            backendSetting = get(),
            jwtSetting = get()
        ) {
            appId = 99
            clientId = "testapp"
            globalDomainUrl = "https://outpost.cmoney.tw/"
            isDebug = BuildConfig.DEBUG
        }
    }
    single { PreferencesManager(context = androidContext()) }
    // Providing the implementation for LoginRepository interface
    single<LoginRepository> { LoginRepositoryImpl(get()) }

    // Define LoginViewModel with required dependencies
    viewModel {
        LoginViewModel(
            loginRepository = get(),
            identityProviderWeb = get()
        )
    }
}