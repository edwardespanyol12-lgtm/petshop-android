package com.example.petshop.screens.products

import com.example.petshop.model.Product

class ProductPresenter(
    private val view: ProductContract.View,
    private val model: ProductModel
) : ProductContract.Presenter {

    override fun loadProducts(category: String) {
        view.showLoading(true)
        val products = model.getProductsByCategory(category)
        view.showLoading(false)
        view.showProducts(products)
    }

    override fun searchProducts(query: String) {
        val products = model.searchProducts(query)
        view.showProducts(products)
    }

    override fun onAddToCartClicked(product: Product) {
        // Logic to add to cart (could call a Repository later)
        view.showAddedToCart(product.name)
    }

    override fun onCartClicked() {
        view.navigateToCart()
    }
}
