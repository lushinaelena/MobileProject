package com.example.mobileproject.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobileproject.Model.ItemsModel
import com.example.mobileproject.databinding.ItemCartBinding
import java.text.NumberFormat
import java.util.*

class CartAdapter(
    private val cartItems: MutableList<ItemsModel>,
    private val onDeleteItem: (ItemsModel) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        with(holder.binding) {
            cartTitle.text = item.title
            cartPrice.text = formatPrice(item.price)

            val imageUrl = item.picUrl.firstOrNull()
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(holder.itemView.context)
                    .load(imageUrl)
                    .into(cartImage)
            } else {
                cartImage.setImageResource(android.R.color.darker_gray) // placeholder
            }

            deleteButton.setOnClickListener {
                onDeleteItem(item)
            }
        }
    }

    override fun getItemCount(): Int = cartItems.size

    fun removeItem(item: ItemsModel) {
        val index = cartItems.indexOf(item)
        if (index != -1) {
            cartItems.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    private fun formatPrice(price: Double): String {
        val format = NumberFormat.getNumberInstance(Locale("ru", "RU"))
        return "${format.format(price)} ₽"
    }
}

