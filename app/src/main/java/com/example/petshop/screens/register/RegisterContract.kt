package com.example.petshop.screens.register

interface RegisterContract {
    interface View {
        fun showSuccess()
        fun showError(message: String)
        fun finishScreen()
    }

    interface Presenter {
        fun onRegisterClicked(username: String, password: String)
    }
}
