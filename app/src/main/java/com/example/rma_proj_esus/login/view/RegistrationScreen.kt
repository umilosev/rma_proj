package com.example.rma_proj_esus.login.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import com.example.rma_proj_esus.login.datastore.UserPreferencesKeys.EMAIL_KEY
import com.example.rma_proj_esus.login.datastore.UserPreferencesKeys.LOGIN_STATE_KEY
import com.example.rma_proj_esus.login.datastore.userPreferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(
    onRegisterSuccess: () -> Unit
) {
    val context = LocalContext.current
    val dataStore = context.userPreferencesDataStore

    var email by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        TextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text("Nickname") }
        )
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                dataStore.edit { preferences ->
                    preferences[LOGIN_STATE_KEY] = true
                    preferences[EMAIL_KEY] = email
                }
                onRegisterSuccess()
            }
        }) {
            Text("Register")
        }
    }
}
