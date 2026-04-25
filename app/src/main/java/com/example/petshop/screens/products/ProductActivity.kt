package com.example.petshop.screens.products

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petshop.databinding.ActivityProductsBinding
import com.example.petshop.model.Product
import com.example.petshop.screens.dashboard.DashboardActivity
import com.example.petshop.screens.profile.ProfileActivity
import com.google.android.material.chip.Chip

class ProductActivity : AppCompatActivity(), ProductContract.View {

    private lateinit var binding: ActivityProductsBinding
    private lateinit var presenter: ProductPresenter
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = ProductPresenter(this, ProductModel())
        setupRecyclerView()
        setupInteractions()

        presenter.loadProducts()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(emptyList()) { product ->
            presenter.onAddToCartClicked(product)
        }
        binding.rvProducts.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.adapter = adapter
    }

    private fun setupInteractions() {
        // Search functionality
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.searchProducts(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Category clicks
        binding.chipGroupCategories.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val category = chip?.text?.toString() ?: "All"
            presenter.loadProducts(category)
        }

        // Action buttons
        binding.btnCart.setOnClickListener { presenter.onCartClicked() }
        binding.btnMenu.setOnClickListener { 
            Toast.makeText(this, "Menu coming soon!", Toast.LENGTH_SHORT).show()
        }

        // Bottom Navigation logic
        binding.navHome.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
        binding.navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
        binding.navProducts.setOnClickListener { /* Already here */ }
        binding.navCart.setOnClickListener { presenter.onCartClicked() }
    }

    override fun showProducts(products: List<Product>) {
        adapter.updateData(products)
    }

    override fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showAddedToCart(productName: String) {
        Toast.makeText(this, "$productName added to cart! 🛒", Toast.LENGTH_SHORT).show()
    }

    override fun navigateToCart() {
        Toast.makeText(this, "Navigating to Cart...", Toast.LENGTH_SHORT).show()
        // Intent to CartActivity when ready
    }
}
