import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rma_proj_esus.login.view.contract.LoginContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferenceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginContract.LoginState())
    val state: StateFlow<LoginContract.LoginState> = _state

    init {
        observeUserPreferences()
    }

    private fun observeUserPreferences() {
        // Collect the username flow
        userPreferencesRepository.userNameFlow
            .onEach { username ->
                _state.update { it.copy(username = username) }
                validateLoginButton()
                Log.d("APP", "Updating username - $username")
            }
            .launchIn(viewModelScope)

        // Collect the email flow
        userPreferencesRepository.emailFlow
            .onEach { email ->
                _state.update { it.copy(email = email) }
                validateLoginButton()
                Log.d("APP", "Updating email - $email")
            }
            .launchIn(viewModelScope)

        Log.d(
            "APP",
            "Test test 123 loading flows - " + state.value.email + " | " + state.value.username + " | " + state.value.isLoginButtonEnabled
        )
        // trigger recomposition
    }

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
                Log.d("APP", "-_-")
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
        val globalScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

        saveLoginCredentials(state.value.username, state.value.email)

        runBlocking {
            userPreferencesRepository.setLoginState(true)
        }

    }

    private fun saveLoginCredentials(username: String, email: String) {
        Log.d("APP", "SAVING : $username : $email")

        runBlocking {
            userPreferencesRepository.saveUserPreferences(username, email)
        }
    }
}
