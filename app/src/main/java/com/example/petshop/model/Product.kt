package com.example.petshop.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val stock: Int,
    val imageResId: Int? = null,
    val imageUrl: String? = null
)
