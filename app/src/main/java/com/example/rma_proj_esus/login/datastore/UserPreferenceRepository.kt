import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferenceRepository(private val context: Context) {

    private object PreferencesKeys {
        val USERNAME = stringPreferencesKey("username")
        val EMAIL = stringPreferencesKey("email")
        val LOGIN_STATE = booleanPreferencesKey("login_state") // Added key for login state
    }

    val userNameFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USERNAME] ?: ""
        }

    val emailFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.EMAIL] ?: ""
        }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.LOGIN_STATE] ?: false
        }

    suspend fun saveUserPreferences(username: String, email: String) {
        Log.d("APP", "Attempting to save login credentials.... $username - $email")

        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USERNAME] = username
            preferences[PreferencesKeys.EMAIL] = email
        }
    }

    suspend fun setLoginState(isLoggedIn: Boolean) {
        try {
            context.dataStore.edit { preferences ->
                preferences[PreferencesKeys.LOGIN_STATE] = isLoggedIn
            }
        } catch (e: Exception) {
            Log.e("DataStoreError", "Error setting login state", e)
        }
    }

    suspend fun logDatastore() {
         Log.d("APP", "Logging datastore to begin with")

         context.dataStore.data
             .map { preferences ->
                 if (preferences.contains(PreferencesKeys.USERNAME) || preferences.contains(PreferencesKeys.EMAIL)) {
                     "DataStore contains data."
                 } else {
                     "DataStore is empty."
                 }
             }
             .catch { e ->
                 Log.e("APP", "Error checking DataStore", e)
             }
             .collect { message ->
                 // Here you process the emitted value from the map transformation
                 Log.d("APP", message)
             }
    }
}
