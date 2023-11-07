import PreferencesKeys.ACCOUNT_VISIBLE_KEY
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 在Kotlin檔案頂層建立DataStore實例
val Context.dataStore by preferencesDataStore(name = "settings")

private object PreferencesKeys {
    val ACCOUNT_VISIBLE_KEY = booleanPreferencesKey("account_visible")
}

// 從Preferences DataStore讀取內容
class PreferencesManager(private val context: Context) {

    // 建立Flow以監聽變化
    val isPasswordVisible: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ACCOUNT_VISIBLE_KEY] ?: true
        }

    // 向Preferences DataStore寫入數據
    suspend fun updateAccountVisibility(isVisible: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ACCOUNT_VISIBLE_KEY] = isVisible
        }
    }
}
