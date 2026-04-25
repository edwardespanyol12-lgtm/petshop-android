package com.example.petshop.screens.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petshop.databinding.ActivityDashboardBinding
import com.example.petshop.model.DashboardResponse
import com.example.petshop.network.RetrofitClient
import com.example.petshop.screens.login.LoginActivity
import com.example.petshop.screens.products.ProductActivity
import com.example.petshop.screens.profile.ProfileActivity
import com.example.petshop.utils.NetworkUtils
import com.example.petshop.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        RetrofitClient.init(sessionManager)

        setupNavigation()
        loadDashboard()
    }

    private fun setupNavigation() {
        binding.navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        binding.navProducts.setOnClickListener {
            startActivity(Intent(this, ProductActivity::class.java))
        }
        binding.btnShopNow.setOnClickListener {
            startActivity(Intent(this, ProductActivity::class.java))
        }
        // Home is already active
        binding.navHome.setOnClickListener { /* already here */ }

        // Hero banner "Shop Now" button (it's a Button inside the RelativeLayout, I should find its ID)
        // Wait, looking at activity_dashboard.xml lines 102-112, the button has NO ID.
    }

    private fun loadDashboard() {
        if (!NetworkUtils.isConnected(this)) {
            Toast.makeText(this, "No internet connection.", Toast.LENGTH_SHORT).show()
            // Show cached user name
            val name = sessionManager.getUserName()
            if (!name.isNullOrEmpty()) {
                binding.tvWelcomeMessage.text = "Welcome back, $name! 🐾"
            }
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        RetrofitClient.instance.getDashboard().enqueue(object : Callback<DashboardResponse> {
            override fun onResponse(call: Call<DashboardResponse>, response: Response<DashboardResponse>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    binding.tvWelcomeMessage.text = data?.welcomeMessage ?: "Welcome! 🐾"
                } else if (response.code() == 401) {
                    redirectToLogin()
                } else {
                    val name = sessionManager.getUserName()
                    binding.tvWelcomeMessage.text = "Welcome, ${name ?: "Pet Lover"}! 🐾"
                }
            }

            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                val name = sessionManager.getUserName()
                binding.tvWelcomeMessage.text = "Welcome, ${name ?: "Pet Lover"}! 🐾"
                Toast.makeText(this@DashboardActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun redirectToLogin() {
        sessionManager.clearSession()
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }
}
