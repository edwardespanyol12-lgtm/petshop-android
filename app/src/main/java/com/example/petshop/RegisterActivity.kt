package com.example.petshop

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petshop.databinding.ActivityRegisterBinding
import com.example.petshop.model.RegisterRequest
import com.example.petshop.model.RegisterResponse
import com.example.petshop.network.RetrofitClient
import com.example.petshop.utils.NetworkUtils
import com.example.petshop.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RetrofitClient.init(SessionManager(this))

        binding.btnRegister.setOnClickListener { attemptRegister() }
        binding.tvLoginLink.setOnClickListener { finish() }
    }

    private fun attemptRegister() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        // Validate
        var hasError = false
        if (name.isEmpty()) { binding.tilName.error = "Name is required"; hasError = true } else binding.tilName.error = null
        if (email.isEmpty()) { binding.tilEmail.error = "Email is required"; hasError = true } else binding.tilEmail.error = null
        if (password.isEmpty()) { binding.tilPassword.error = "Password is required"; hasError = true } else binding.tilPassword.error = null
        if (password.length < 6) { binding.tilPassword.error = "Password must be at least 6 characters"; hasError = true }
        if (confirmPassword != password) { binding.tilConfirmPassword.error = "Passwords do not match"; hasError = true } else binding.tilConfirmPassword.error = null
        if (hasError) return

        if (!NetworkUtils.isConnected(this)) {
            Toast.makeText(this, "No internet connection. Please check your network.", Toast.LENGTH_LONG).show()
            return
        }

        setLoading(true)

        val request = RegisterRequest(name, email, password)
        RetrofitClient.instance.register(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                setLoading(false)
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Account created successfully! Please login. 🎉", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } else {
                    val code = response.code()
                    val msg = when (code) {
                        400 -> "Email already registered or invalid data."
                        500 -> "Server error. Please try again later."
                        else -> "Registration failed (Error $code)."
                    }
                    Toast.makeText(this@RegisterActivity, msg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                setLoading(false)
                Toast.makeText(this@RegisterActivity, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !loading
        binding.etName.isEnabled = !loading
        binding.etEmail.isEnabled = !loading
        binding.etPassword.isEnabled = !loading
        binding.etConfirmPassword.isEnabled = !loading
    }
}
