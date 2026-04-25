package com.example.petshop.screens.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petshop.R
import com.example.petshop.model.Product
import com.google.android.material.button.MaterialButton

class ProductAdapter(
    private var products: List<Product>,
    private val onAddToCart: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProductImage: ImageView = view.findViewById(R.id.ivProductImage)
        val tvProductName: TextView = view.findViewById(R.id.tvProductName)
        val tvProductDescription: TextView = view.findViewById(R.id.tvProductDescription)
        val tvProductPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val tvProductStock: TextView = view.findViewById(R.id.tvProductStock)
        val btnAddToCart: MaterialButton = view.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.tvProductName.text = product.name
        holder.tvProductDescription.text = product.description
        holder.tvProductPrice.text = "$${String.format("%.2f", product.price)}"
        holder.tvProductStock.text = "Stock: ${product.stock} available"
        
        // In a real app we'd load image with Glide/Picasso
        // holder.ivProductImage.setImageResource(product.imageResId ?: R.drawable.gradient_banner)

        holder.btnAddToCart.setOnClickListener { onAddToCart(product) }
    }

    override fun getItemCount() = products.size

    fun updateData(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
