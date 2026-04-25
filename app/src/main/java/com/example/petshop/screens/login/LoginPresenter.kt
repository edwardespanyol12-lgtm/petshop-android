package com.example.petshop.screens.login

class LoginPresenter(private val view: LoginContract.View, private val model: LoginModel) :
    LoginContract.Presenter {

    override fun onLoginClicked(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            view.showError("Fields cannot be empty")
            return
        }

        val success = model.login(username, password)
        if (success) {
            view.showSuccess()
            view.navigateToDashboard()
        } else {
            view.showError("Invalid credentials")
        }
    }

    override fun onRegisterClicked() {
        view.navigateToRegister()
    }
}
