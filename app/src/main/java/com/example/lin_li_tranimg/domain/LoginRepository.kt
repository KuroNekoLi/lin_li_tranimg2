package com.example.lin_li_tranimg.domain

import com.cmoney.backend2.identityprovider.service.api.gettoken.GetTokenResponseBody
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun savedAccountFlow(): Flow<String?>
    fun savedPasswordFlow(): Flow<String?>
    fun isAccountVisibleFlow(): Flow<Boolean>
    suspend fun saveUserData(account: String, password: String)
    suspend fun clearUserData()
    suspend fun updateAccountVisibility(visible: Boolean)
    suspend fun loginByEmail(account: String, password: String): Result<GetTokenResponseBody>
}