package com.example.mobileproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobileproject.Model.ItemsModel
import com.example.mobileproject.R
import com.example.mobileproject.databinding.ItemCartBinding

class FavoriteAdapter(
    private val favoriteItems: MutableList<ItemsModel>,
    private val onDeleteItem: (ItemsModel) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = favoriteItems[position]
        holder.binding.apply {
            cartTitle.text = item.title
            cartPrice.text = "${item.price}₽"

            Glide.with(holder.itemView.context)
                .load(item.picUrl.firstOrNull())
                .into(cartImage)

            deleteButton.setOnClickListener {
                onDeleteItem(item)
            }
        }
    }

    override fun getItemCount(): Int = favoriteItems.size

    // Метод для удаления товара из списка
    fun removeItem(position: Int) {
        favoriteItems.removeAt(position)
        notifyItemRemoved(position)
    }
}

