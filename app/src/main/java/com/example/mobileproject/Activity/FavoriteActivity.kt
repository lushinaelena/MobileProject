package com.example.mobileproject.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileproject.Adapter.FavoriteAdapter
import com.example.mobileproject.Model.ItemsModel
import com.example.mobileproject.databinding.ActivityFavoriteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var favoritesListener: ValueEventListener
    private val favoriteItems = mutableListOf<ItemsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Favorites")
            .child(auth.currentUser?.uid ?: "")

        setupRecyclerView()
        observeFavoritesInRealtime()

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = FavoriteAdapter(favoriteItems) { item ->
            deleteItemFromFavorites(item)
        }

        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.favoriteRecyclerView.adapter = adapter
    }

    private fun observeFavoritesInRealtime() {
        favoritesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                favoriteItems.clear()
                for (child in snapshot.children) {
                    val item = child.getValue(ItemsModel::class.java)
                    item?.let { favoriteItems.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FavoriteActivity, "Ошибка загрузки избранного", Toast.LENGTH_SHORT).show()
            }
        }

        database.addValueEventListener(favoritesListener)
    }

    private fun deleteItemFromFavorites(item: ItemsModel) {
        database.orderByChild("title").equalTo(item.title)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        child.ref.removeValue()
                    }
                    Toast.makeText(this@FavoriteActivity, "Удалено из избранного", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@FavoriteActivity, "Ошибка при удалении", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        database.removeEventListener(favoritesListener)
    }
}
