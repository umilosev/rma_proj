import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.rma_proj_esus.cats.details.CatsDetailsViewModel
import com.example.rma_proj_esus.cats.leaderboard.LeaderboardViewModel
import com.example.rma_proj_esus.cats.repository.LeaderboardRepository.fetchLeaderboardByCategory
import com.example.rma_proj_esus.login.view.contract.LoginContract
import com.example.rma_proj_esus.login.view.factory.LoginViewModelFactory
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.loginScreen(
    route: String,
    navController: NavController,
    userPreferencesRepository: UserPreferenceRepository
) = composable(route = route) {
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(userPreferencesRepository)
    )

    val state by viewModel.state.collectAsState()

    LoginScreen(
        userPreferencesRepository = userPreferencesRepository,
        onLoginSuccess = {
            navController.navigate("cats") {
                popUpTo("login") { inclusive = true }
            }
        }
    )
}

@Composable
fun LoginScreen(
    userPreferencesRepository: UserPreferenceRepository,
    onLoginSuccess: () -> Unit
) {
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(userPreferencesRepository)
    )
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.username,
            onValueChange = { viewModel.handleEvent(LoginContract.LoginEvent.UsernameChanged(it)) },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.handleEvent(LoginContract.LoginEvent.EmailChanged(it)) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

//                viewModel.handleEvent(LoginContract.LoginEvent.LoginClicked)
//
//                Log.d("APP", "State may have changed - " + state.email + " | "
//                        + state.username + " | "
//                        + state.isLoginButtonEnabled)
//
//                onLoginSuccess()
            },
            enabled = state.isLoginButtonEnabled,
            modifier = Modifier.background(Color.Red)
        ) {
            Text("Login")
        }
    }
}
