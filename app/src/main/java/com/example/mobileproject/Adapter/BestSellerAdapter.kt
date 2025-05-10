package com.example.mobileproject.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobileproject.Model.ItemsModel
import com.example.mobileproject.databinding.ViewholderBestSellerBinding

class BestSellerAdapter(val items:MutableList<ItemsModel>):
        RecyclerView.Adapter<BestSellerAdapter.Viewholder>(){
    class Viewholder(val binding:ViewholderBestSellerBinding):
    RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestSellerAdapter.Viewholder {
        val binding=ViewholderBestSellerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: BestSellerAdapter.Viewholder, position: Int) {
        holder.binding.Price.text=items[position].price.toString()+" РУБ"
        holder.binding.Title.text=items[position].title

        Glide.with(holder.itemView.context)
            .load(items[position].picUrl[0])
            .into(holder.binding.hot1)
    }

    override fun getItemCount(): Int =items.size
}
