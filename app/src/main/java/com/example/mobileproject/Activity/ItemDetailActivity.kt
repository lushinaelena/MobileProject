package com.example.mobileproject.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mobileproject.Adapter.SizeAdapter
import com.example.mobileproject.Model.ItemsModel
import com.example.mobileproject.databinding.ActivityItemDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemDetailBinding
    private var selectedSize: String? = null
    private lateinit var item: ItemsModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        item = intent.getSerializableExtra("item") as ItemsModel
        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            Title.text = item.title
            Description.text = item.description
            Price.text = "${item.price} РУБ"

            Glide.with(this@ItemDetailActivity)
                .load(item.picUrl.firstOrNull())
                .into(ItemImage)

            iconNazad.setOnClickListener {
                finish()
            }

            val sizeAdapter = SizeAdapter(item.size) { size ->
                selectedSize = size
            }
            SizeRecyclerView.layoutManager = LinearLayoutManager(
                this@ItemDetailActivity, LinearLayoutManager.HORIZONTAL, false
            )
            SizeRecyclerView.adapter = sizeAdapter

            AddToCartButton.setOnClickListener {
                if (selectedSize == null) {
                    Toast.makeText(
                        this@ItemDetailActivity,
                        "Выберите размер",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    addToCart()
                }
            }

            AddToFavoriteButton.setOnClickListener {
                addToFavorite()
            }
        }
    }

    private fun addToCart() {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Пожалуйста, войдите в аккаунт", Toast.LENGTH_SHORT).show()
            return
        }

        val cartRef = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(userId)
            .child("Cart")

        val cartItemId = cartRef.push().key
        if (cartItemId != null) {
            cartRef.child(cartItemId).setValue(item)
                .addOnSuccessListener {
                    Toast.makeText(this, "Добавлено в корзину", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Ошибка при добавлении в корзину", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun addToFavorite() {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Пожалуйста, войдите в аккаунт", Toast.LENGTH_SHORT).show()
            return
        }

        val favoriteRef = FirebaseDatabase.getInstance()
            .getReference("Favorites")
            .child(userId)

        val favoriteItemId = favoriteRef.push().key
        if (favoriteItemId != null) {
            favoriteRef.child(favoriteItemId).setValue(item)
                .addOnSuccessListener {
                    Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Ошибка при добавлении в избранное", Toast.LENGTH_SHORT).show()
                }
        }
    }
}



