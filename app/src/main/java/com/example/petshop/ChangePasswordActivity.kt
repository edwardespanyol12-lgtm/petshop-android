package com.example.petshop

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petshop.databinding.ActivityChangePasswordBinding
import com.example.petshop.model.ChangePasswordRequest
import com.example.petshop.model.MessageResponse
import com.example.petshop.network.RetrofitClient
import com.example.petshop.utils.NetworkUtils
import com.example.petshop.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RetrofitClient.init(SessionManager(this))

        binding.btnBack.setOnClickListener { finish() }
        binding.btnChangePassword.setOnClickListener { attemptChangePassword() }
    }

    private fun attemptChangePassword() {
        val current = binding.etCurrentPassword.text.toString().trim()
        val newPass = binding.etNewPassword.text.toString().trim()
        val confirm = binding.etConfirmPassword.text.toString().trim()

        // Validate
        var hasError = false
        if (current.isEmpty()) { binding.tilCurrentPassword.error = "Current password is required"; hasError = true } else binding.tilCurrentPassword.error = null
        if (newPass.isEmpty()) { binding.tilNewPassword.error = "New password is required"; hasError = true } else binding.tilNewPassword.error = null
        if (newPass.length < 6) { binding.tilNewPassword.error = "Password must be at least 6 characters"; hasError = true }
        if (confirm != newPass) { binding.tilConfirmPassword.error = "Passwords do not match"; hasError = true } else binding.tilConfirmPassword.error = null
        if (hasError) return

        if (!NetworkUtils.isConnected(this)) {
            Toast.makeText(this, "No internet connection.", Toast.LENGTH_LONG).show()
            return
        }

        setLoading(true)

        val request = ChangePasswordRequest(current, newPass)
        RetrofitClient.instance.changePassword(request).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                setLoading(false)
                if (response.isSuccessful) {
                    Toast.makeText(this@ChangePasswordActivity, "Password changed successfully! 🔐", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val code = response.code()
                    val msg = when (code) {
                        401 -> "Current password is incorrect."
                        400 -> "Invalid data. Please check your inputs."
                        500 -> "Server error. Please try again later."
                        else -> "Failed to change password (Error $code)."
                    }
                    Toast.makeText(this@ChangePasswordActivity, msg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                setLoading(false)
                Toast.makeText(this@ChangePasswordActivity, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnChangePassword.isEnabled = !loading
        binding.etCurrentPassword.isEnabled = !loading
        binding.etNewPassword.isEnabled = !loading
        binding.etConfirmPassword.isEnabled = !loading
    }
}
