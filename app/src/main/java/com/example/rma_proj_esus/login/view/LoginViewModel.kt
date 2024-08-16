import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(private val userPreferencesRepository: UserPreferenceRepository) : ViewModel() {

    val userName = userPreferencesRepository.userNameFlow
    val email = userPreferencesRepository.emailFlow

    fun saveLoginCredentials(username: String, email: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveUserPreferences(username, email)
        }
    }
}
