package com.example.lin_li_tranimg.util

import com.example.lin_li_tranimg.util.PreferencesKeys.ACCOUNT_KEY
import com.example.lin_li_tranimg.util.PreferencesKeys.ACCOUNT_VISIBLE_KEY
import com.example.lin_li_tranimg.util.PreferencesKeys.PASSWORD_KEY
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

private object PreferencesKeys {
    val ACCOUNT_VISIBLE_KEY = booleanPreferencesKey("account_visible")
    val PASSWORD_KEY = stringPreferencesKey("remembered_password")
    val ACCOUNT_KEY = stringPreferencesKey("remembered_account")
}
/**
 * 儲存用戶偏好設置
 */
class PreferencesManager(private val context: Context) {

    /**
     * 用戶帳號可見狀態
     */
    val isAccountVisible: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ACCOUNT_VISIBLE_KEY] ?: true
        }
    /**
     * 用戶儲存的密碼
     */
    val passwordFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PASSWORD_KEY]
        }
    /**
     * 用戶儲存的帳號
     */
    val accountFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[ACCOUNT_KEY]
        }
    /**
     * 用戶儲存的眼睛圖示狀態
     */
    suspend fun updateAccountVisibility(isVisible: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ACCOUNT_VISIBLE_KEY] = isVisible
        }
    }
    /**
     * 儲存用戶的帳號與密碼
     * @param account 用戶的帳號
     * @param password 用戶的密碼
     */
    suspend fun saveUserData(account: String, password: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCOUNT_KEY] = account
            preferences[PASSWORD_KEY] = password
        }
    }
    /**
     * 清空儲存的用戶資料
     */
    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences[ACCOUNT_KEY] = ""
            preferences[PASSWORD_KEY] = ""
        }
    }
}
