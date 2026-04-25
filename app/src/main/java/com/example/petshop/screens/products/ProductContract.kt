package com.example.petshop.screens.products

import com.example.petshop.model.Product

interface ProductContract {
    interface View {
        fun showProducts(products: List<Product>)
        fun showLoading(loading: Boolean)
        fun showError(message: String)
        fun showAddedToCart(productName: String)
        fun navigateToCart()
    }

    interface Presenter {
        fun loadProducts(category: String = "All")
        fun searchProducts(query: String)
        fun onAddToCartClicked(product: Product)
        fun onCartClicked()
    }
}
