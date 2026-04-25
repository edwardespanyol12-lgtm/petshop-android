package com.example.petshop.screens.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.petshop.databinding.ActivityProfileBinding
import com.example.petshop.model.ProfileResponse
import com.example.petshop.network.RetrofitClient
import com.example.petshop.screens.login.LoginActivity
import com.example.petshop.utils.NetworkUtils
import com.example.petshop.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        RetrofitClient.init(sessionManager)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(this, UpdateProfileActivity::class.java))
        }
        binding.btnChangePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
        binding.btnLogout.setOnClickListener { confirmLogout() }

        // Show cached name/email immediately
        binding.tvUserName.text = sessionManager.getUserName() ?: "Loading..."
        binding.tvUserEmail.text = sessionManager.getUserEmail() ?: ""

        loadProfile()
    }

    override fun onResume() {
        super.onResume()
        loadProfile() // Refresh after UpdateProfile
    }

    private fun loadProfile() {
        if (!NetworkUtils.isConnected(this)) {
            Toast.makeText(this, "No internet connection.", Toast.LENGTH_SHORT).show()
            showCachedProfile()
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        RetrofitClient.instance.getProfile().enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val profile = response.body()?.profile
                    binding.tvUserName.text = profile?.name ?: "—"
                    binding.tvUserEmail.text = profile?.email ?: "—"
                    binding.tvName.text = profile?.name ?: "—"
                    binding.tvEmail.text = profile?.email ?: "—"
                    binding.tvPhone.text = if (profile?.phone.isNullOrEmpty()) "Not set" else profile?.phone
                } else if (response.code() == 401) {
                    redirectToLogin()
                } else {
                    showCachedProfile()
                    Toast.makeText(this@ProfileActivity, "Could not load profile (Error ${response.code()}).", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                showCachedProfile()
                Toast.makeText(this@ProfileActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showCachedProfile() {
        val name = sessionManager.getUserName() ?: "—"
        val email = sessionManager.getUserEmail() ?: "—"
        binding.tvUserName.text = name
        binding.tvUserEmail.text = email
        binding.tvName.text = name
        binding.tvEmail.text = email
        binding.tvPhone.text = "Not set"
    }

    private fun confirmLogout() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                sessionManager.clearSession()
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun redirectToLogin() {
        sessionManager.clearSession()
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }
}
