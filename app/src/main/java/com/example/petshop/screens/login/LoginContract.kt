package com.example.petshop.screens.login

interface LoginContract {
    interface View {
        fun showSuccess()
        fun showError(message: String)
        fun navigateToRegister()
        fun navigateToDashboard()
    }

    interface Presenter {
        fun onLoginClicked(username: String, password: String)
        fun onRegisterClicked()
    }
}
