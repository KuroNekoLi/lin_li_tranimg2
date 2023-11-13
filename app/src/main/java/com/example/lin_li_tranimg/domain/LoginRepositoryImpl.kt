package com.example.lin_li_tranimg.domain

import PreferencesManager
import com.cmoney.backend2.identityprovider.service.IdentityProviderWeb
import com.cmoney.backend2.identityprovider.service.api.gettoken.GetTokenResponseBody
import kotlinx.coroutines.flow.Flow
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginRepositoryImpl(
    private val preferencesManager: PreferencesManager,
    private val identityProviderWeb: IdentityProviderWeb
) : LoginRepository {
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


    override suspend fun loginByEmail(
        account: String,
        password: String
    ): Result<GetTokenResponseBody> {
        val md5edPassword = try {
            password.md5().orEmpty()
        } catch (e: Exception) {
            return Result.failure(e)
        }

        if (account.isEmpty() || md5edPassword.isEmpty()) {
            return Result.failure(IllegalArgumentException("帳號或密碼不可為空"))
        }

        return identityProviderWeb.loginByEmail(account, md5edPassword)
    }

    @Throws(NoSuchAlgorithmException::class)
    private fun String.md5(): String? {
        val md5: MessageDigest = MessageDigest.getInstance("MD5")
        val digest: ByteArray = md5.digest(this.toByteArray(StandardCharsets.UTF_8))
        return java.lang.String.format("%032x", BigInteger(1, digest))
    }
}