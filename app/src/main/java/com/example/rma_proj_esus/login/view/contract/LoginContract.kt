package com.example.rma_proj_esus.login.view.contract

sealed class LoginContract {
    data class LoginState(
        val username: String = "",
        val email: String = "",
        val isLoginButtonEnabled: Boolean = false
    )

    sealed class LoginEvent {
        data class UsernameChanged(val username: String) : LoginEvent()
        data class EmailChanged(val email: String) : LoginEvent()
        object LoginClicked : LoginEvent()
    }
}
