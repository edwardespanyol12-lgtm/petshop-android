package com.example.petshop.screens.products

import com.example.petshop.model.Product

class ProductModel {
    private val allProducts = listOf(
        Product(
            id = 1,
            name = "Premium Dog Collar",
            description = "Durable and stylish collar for your beloved pet. Made with high-quality materials for maximum comfort and durability.",
            price = 24.99,
            category = "Collars",
            stock = 15
        ),
        Product(
            id = 2,
            name = "Luxury Leather Leash",
            description = "Elegant leather leash with heavy-duty clasp. Perfect for long walks and training sessions.",
            price = 34.99,
            category = "Leashes",
            stock = 8
        ),
        Product(
            id = 3,
            name = "Interactive Squeaky Toy",
            description = "Keep your dog entertained for hours with this fun squeaky toy. Soft yet durable construction.",
            price = 12.50,
            category = "Toys",
            stock = 25
        ),
        Product(
            id = 4,
            name = "Nylon Safety Collar",
            description = "Lightweight and reflective nylon collar for night visibility. Adjustable size for all breeds.",
            price = 15.99,
            category = "Collars",
            stock = 30
        ),
        Product(
            id = 5,
            name = "Rope Tug of War Toy",
            description = "Heavy duty natural cotton rope for aggressive chewers. Great for dental health.",
            price = 18.00,
            category = "Toys",
            stock = 12
        )
    )

    fun getProductsByCategory(category: String): List<Product> {
        if (category == "All") return allProducts
        return allProducts.filter { it.category == category }
    }

    fun searchProducts(query: String): List<Product> {
        if (query.isEmpty()) return allProducts
        return allProducts.filter { 
            it.name.contains(query, ignoreCase = true) || 
            it.description.contains(query, ignoreCase = true) 
        }
    }
}
