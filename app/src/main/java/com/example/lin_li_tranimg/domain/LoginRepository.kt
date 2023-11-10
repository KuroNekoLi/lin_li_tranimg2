package com.example.lin_li_tranimg.domain

import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun savedAccountFlow(): Flow<String?>
    fun savedPasswordFlow(): Flow<String?>
    fun isAccountVisibleFlow(): Flow<Boolean>
    suspend fun saveUserData(account: String, password: String)
    suspend fun clearUserData()
    suspend fun updateAccountVisibility(visible: Boolean)
}