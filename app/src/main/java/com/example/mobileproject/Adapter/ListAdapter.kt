package com.example.mobileproject.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobileproject.Activity.ItemDetailActivity
import com.example.mobileproject.Model.ItemsModel
import com.example.mobileproject.databinding.ViewholderListBinding

class ListAdapter(private val items: MutableList<ItemsModel>) :
    RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: ViewholderListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ViewholderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            Title.text = item.title
            Price.text = "${item.price} РУБ"
            Glide.with(holder.itemView.context)
                .load(item.picUrl[0])
                .into(hot1)
        }
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ItemDetailActivity::class.java)
            intent.putExtra("item", item)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}
