package com.example.petshop.screens.register

class RegisterPresenter(private val view: RegisterContract.View, private val model: RegisterModel) :
    RegisterContract.Presenter {

    override fun onRegisterClicked(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            view.showError("Fields cannot be empty")
            return
        }

        val success = model.register(username, password)
        if (success) {
            view.showSuccess()
            view.finishScreen()
        } else {
            view.showError("User already exists")
        }
    }
}
