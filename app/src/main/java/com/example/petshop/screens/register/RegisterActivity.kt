package com.example.petshop.screens.register

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petshop.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(), RegisterContract.View {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var presenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = RegisterPresenter(this, RegisterModel())

        binding.btnRegister.setOnClickListener {
            // Mapping email to username as per example logic
            presenter.onRegisterClicked(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        binding.tvLoginLink.setOnClickListener {
            finish()
        }
    }

    override fun showSuccess() {
        Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun finishScreen() {
        finish()
    }
}
