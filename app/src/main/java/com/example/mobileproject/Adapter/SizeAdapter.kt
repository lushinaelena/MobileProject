package com.example.mobileproject.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileproject.databinding.ItemSizeBinding

class SizeAdapter(
    private val sizes: List<String>,
    private val onSizeSelected: (String) -> Unit
) : RecyclerView.Adapter<SizeAdapter.SizeViewHolder>() {

    private var selectedPosition = -1

    inner class SizeViewHolder(val binding: ItemSizeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        val binding = ItemSizeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SizeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        val size = sizes[position]
        holder.binding.SizeText.text = size

        // Выделяем выбранный размер
        holder.binding.SizeText.isSelected = position == selectedPosition

        // Фон зависит от выбранного состояния
        holder.binding.SizeText.setBackgroundColor(
            if (position == selectedPosition) Color.LTGRAY else Color.WHITE
        )


        holder.binding.root.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            onSizeSelected(size)
        }
    }

    override fun getItemCount(): Int = sizes.size
}
