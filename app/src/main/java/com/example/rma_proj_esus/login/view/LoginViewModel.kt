import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rma_proj_esus.login.view.contract.LoginContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferenceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginContract.LoginState())
    val state: StateFlow<LoginContract.LoginState> = _state

    fun handleEvent(event: LoginContract.LoginEvent) {
        when (event) {
            is LoginContract.LoginEvent.UsernameChanged -> {
                _state.update { it.copy(username = event.username) }
                validateLoginButton()
            }
            is LoginContract.LoginEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email) }
                validateLoginButton()
            }
            is LoginContract.LoginEvent.LoginClicked -> {
                handleLogin()
            }
        }
    }

    private fun validateLoginButton() {
        _state.update {
            it.copy(isLoginButtonEnabled = it.username.isNotBlank() && it.email.isNotBlank())
        }
    }

    private fun handleLogin() {
        viewModelScope.launch {
            saveLoginCredentials(state.value.username, state.value.email)
            userPreferencesRepository.setLoginState(true)
        }
    }

    private fun saveLoginCredentials(username: String, email: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveUserPreferences(username, email)
        }
    }
}
