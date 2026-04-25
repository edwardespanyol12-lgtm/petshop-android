package com.example.petshop.screens.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petshop.databinding.ActivityUpdateProfileBinding
import com.example.petshop.model.ProfileResponse
import com.example.petshop.model.UpdateProfileRequest
import com.example.petshop.network.RetrofitClient
import com.example.petshop.utils.NetworkUtils
import com.example.petshop.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateProfileBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        RetrofitClient.init(sessionManager)

        binding.btnBack.setOnClickListener { finish() }

        // Pre-fill with cached data
        binding.etName.setText(sessionManager.getUserName())

        // Load fresh from API
        currentProfile()

        binding.btnSave.setOnClickListener { attemptUpdate() }
    }

    private fun currentProfile() {
        if (!NetworkUtils.isConnected(this)) return

        RetrofitClient.instance.getProfile().enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    val profile = response.body()?.profile
                    binding.etName.setText(profile?.name ?: "")
                    binding.etPhone.setText(profile?.phone ?: "")
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) { /* keep prefilled */ }
        })
    }

    private fun attemptUpdate() {
        val name = binding.etName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()

        if (name.isEmpty()) {
            binding.tilName.error = "Name is required"
            return
        }
        binding.tilName.error = null

        if (!NetworkUtils.isConnected(this)) {
            Toast.makeText(this, "No internet connection.", Toast.LENGTH_LONG).show()
            return
        }

        setLoading(true)

        val request = UpdateProfileRequest(name, phone.ifEmpty { null })
        RetrofitClient.instance.updateProfile(request).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                setLoading(false)
                if (response.isSuccessful) {
                    val updatedName = response.body()?.profile?.name ?: name
                    sessionManager.updateName(updatedName)
                    Toast.makeText(this@UpdateProfileActivity, "Profile updated successfully! ✅", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val code = response.code()
                    val msg = when (code) {
                        400 -> "Invalid data. Please check your inputs."
                        401 -> "Unauthorized. Please login again."
                        500 -> "Server error. Please try again later."
                        else -> "Update failed (Error $code)."
                    }
                    Toast.makeText(this@UpdateProfileActivity, msg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                setLoading(false)
                Toast.makeText(this@UpdateProfileActivity, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnSave.isEnabled = !loading
        binding.etName.isEnabled = !loading
        binding.etPhone.isEnabled = !loading
    }
}
