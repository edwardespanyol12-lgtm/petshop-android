package com.example.petshop.screens.login

import com.example.petshop.data.UserRepository

class LoginModel {
    fun login(username: String, password: String): Boolean {
        return UserRepository.login(username, password)
    }
}
