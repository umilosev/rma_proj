import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferenceRepository(private val context: Context) {

    private object PreferencesKeys {
        val USERNAME = stringPreferencesKey("username")
        val EMAIL = stringPreferencesKey("email")
    }

    val userNameFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USERNAME] ?: ""
        }

    val emailFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.EMAIL] ?: ""
        }

    suspend fun saveUserPreferences(username: String, email: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USERNAME] = username
            preferences[PreferencesKeys.EMAIL] = email
        }
    }
}
