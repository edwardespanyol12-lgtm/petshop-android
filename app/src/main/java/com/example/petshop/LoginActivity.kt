package com.example.petshop

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petshop.databinding.ActivityLoginBinding
import com.example.petshop.model.LoginRequest
import com.example.petshop.model.LoginResponse
import com.example.petshop.network.RetrofitClient
import com.example.petshop.utils.NetworkUtils
import com.example.petshop.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        RetrofitClient.init(sessionManager)

        binding.btnLogin.setOnClickListener { attemptLogin() }
        binding.tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun attemptLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // Validate
        if (email.isEmpty()) {
            binding.tilEmail.error = "Email is required"
            return
        }
        binding.tilEmail.error = null
        if (password.isEmpty()) {
            binding.tilPassword.error = "Password is required"
            return
        }
        binding.tilPassword.error = null

        // Check internet
        if (!NetworkUtils.isConnected(this)) {
            Toast.makeText(this, "No internet connection. Please check your network.", Toast.LENGTH_LONG).show()
            return
        }

        setLoading(true)

        val request = LoginRequest(email, password)
        RetrofitClient.instance.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                setLoading(false)
                if (response.isSuccessful) {
                    val body = response.body()
                    val token = body?.token ?: ""
                    val user = body?.user
                    sessionManager.saveSession(
                        token = token,
                        userId = user?.id ?: "",
                        name = user?.name ?: "",
                        email = user?.email ?: email
                    )
                    Toast.makeText(this@LoginActivity, "Welcome back! 🐾", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                    finish()
                } else {
                    val code = response.code()
                    val msg = when (code) {
                        401 -> "Invalid email or password."
                        400 -> "Please check your credentials."
                        500 -> "Server error. Please try again later."
                        else -> "Login failed (Error $code)."
                    }
                    Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                setLoading(false)
                Toast.makeText(this@LoginActivity, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !loading
        binding.etEmail.isEnabled = !loading
        binding.etPassword.isEnabled = !loading
    }
}
