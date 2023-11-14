package com.example.lin_li_tranimg.domain

import com.cmoney.backend2.identityprovider.service.api.gettoken.GetTokenResponseBody
import kotlinx.coroutines.flow.Flow

/**
 * 定義用於管理用戶認證數據的操作的接口。
 */
interface LoginRepository {
    /**
     * 用戶所儲存的帳號 Flow。
     * @return 發射已保存的帳號的 Flow，若無保存的帳號則為 null。
     */
    fun savedAccountFlow(): Flow<String?>
    /**
     * 用戶所儲存的密碼 Flow。
     * @return 發射已保存的密碼的字串 Flow，若無保存的密碼則為 null。
     */
    fun savedPasswordFlow(): Flow<String?>
    /**
     * 帳號可見性狀態 Flow。
     * @return 發射帳號可見性的 Boolean Flow。
     */
    fun isAccountVisibleFlow(): Flow<Boolean>
    /**
     * 保存用戶數據，包括帳號和密碼。
     *
     * @param account 用戶的帳號。
     * @param password 用戶的密碼。
     */
    suspend fun saveUserData(account: String, password: String)
    /**
     * 清除所有已保存的用戶數據。
     */
    suspend fun clearUserData()
    /**
     * 更新帳號的可見性狀態。
     *
     * @param visible 要設置的可見性狀態。
     */
    suspend fun updateAccountVisibility(visible: Boolean)
    /**
     * 使用電子郵件和密碼進行登錄操作。
     *
     * @param account 用戶的電子郵件帳號。
     * @param password 用戶的密碼。
     * @return 登錄操作的結果。
     */
    suspend fun loginByEmail(account: String, password: String): Result<GetTokenResponseBody>
    /**
     * 使用手機和密碼進行登錄操作。
     *
     * @param account 用戶的手機號碼。
     * @param password 用戶的密碼。
     * @return 登錄操作的結果。
     */
    suspend fun loginCellphone(account: String, password: String): Result<GetTokenResponseBody>
}