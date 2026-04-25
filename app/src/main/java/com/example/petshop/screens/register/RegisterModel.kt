package com.example.petshop.screens.register

import com.example.petshop.data.UserRepository

class RegisterModel {
    fun register(username: String, password: String): Boolean {
        return UserRepository.register(username, password)
    }
}
