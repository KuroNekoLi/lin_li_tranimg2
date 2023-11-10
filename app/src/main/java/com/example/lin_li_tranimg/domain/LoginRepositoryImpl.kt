package com.example.lin_li_tranimg.domain

import PreferencesManager
import kotlinx.coroutines.flow.Flow

class LoginRepositoryImpl(private val preferencesManager: PreferencesManager) : LoginRepository {
    override fun savedAccountFlow(): Flow<String?> {
        return preferencesManager.accountFlow
    }

    override fun savedPasswordFlow(): Flow<String?> {
        return preferencesManager.passwordFlow
    }

    override fun isAccountVisibleFlow(): Flow<Boolean> {
        return preferencesManager.isAccountVisible
    }

    override suspend fun saveUserData(account: String, password: String) {
        return preferencesManager.saveUserData(account, password)
    }

    override suspend fun clearUserData() {
        return preferencesManager.clearUserData()
    }

    override suspend fun updateAccountVisibility(visible: Boolean) {
        return preferencesManager.updateAccountVisibility(visible)
    }
}