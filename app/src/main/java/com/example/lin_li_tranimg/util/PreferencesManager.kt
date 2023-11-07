import PreferencesKeys.ACCOUNT_KEY
import PreferencesKeys.ACCOUNT_VISIBLE_KEY
import PreferencesKeys.PASSWORD_KEY
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 在Kotlin檔案頂層建立DataStore實例
val Context.dataStore by preferencesDataStore(name = "settings")

private object PreferencesKeys {
    val ACCOUNT_VISIBLE_KEY = booleanPreferencesKey("account_visible")
    val PASSWORD_KEY = stringPreferencesKey("remembered_password")
    val ACCOUNT_KEY = stringPreferencesKey("remembered_account")
}

// 從Preferences DataStore讀取內容
class PreferencesManager(private val context: Context) {

    val isAccountVisible: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ACCOUNT_VISIBLE_KEY] ?: true
        }

    val passwordFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PASSWORD_KEY]
        }

    val accountFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[ACCOUNT_KEY]
        }

    // 向Preferences DataStore寫入數據
    suspend fun updateAccountVisibility(isVisible: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ACCOUNT_VISIBLE_KEY] = isVisible
        }
    }
    suspend fun savePassword(password: String){
        context.dataStore.edit { preferences ->
            preferences[PASSWORD_KEY] = password
        }
    }

    suspend fun clearPassword() {
        context.dataStore.edit { preferences ->
            preferences[PASSWORD_KEY] = ""
        }
    }

    suspend fun saveAccount(account: String){
        context.dataStore.edit { preferences ->
            preferences[ACCOUNT_KEY] = account
        }
    }

    suspend fun clearAccount() {
        context.dataStore.edit { preferences ->
            preferences[ACCOUNT_KEY] = ""
        }
    }
}
