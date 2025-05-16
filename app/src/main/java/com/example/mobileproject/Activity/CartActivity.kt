package com.example.mobileproject.Activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileproject.Adapter.CartAdapter
import com.example.mobileproject.Model.ItemsModel
import com.example.mobileproject.databinding.ActivityCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter
    private val cartItems = mutableListOf<ItemsModel>()

    private lateinit var databaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var cartListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().getReference("Users")
            .child(auth.currentUser?.uid ?: "")
            .child("Cart")

        setupRecyclerView()
        observeCartChanges()

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(cartItems) { item ->
            deleteItemFromFirebase(item)
        }

        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = cartAdapter
        }
    }

    private fun observeCartChanges() {
        binding.progressBar.visibility = View.VISIBLE

        cartListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartItems.clear()
                var totalPrice = 0.0
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(ItemsModel::class.java)
                    if (item != null) {
                        cartItems.add(item)
                        val priceDouble = try {
                            item.price.toDouble()
                        } catch (e: Exception) {
                            0.0
                        }
                        totalPrice += priceDouble
                    }
                }
                cartAdapter.notifyDataSetChanged()
                updateEmptyState()
                updateTotalPrice(totalPrice)
                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CartActivity, "Ошибка загрузки: ${error.message}", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        }

        databaseRef.addValueEventListener(cartListener)
    }

    private fun deleteItemFromFirebase(item: ItemsModel) {
        val query = databaseRef.orderByChild("title").equalTo(item.title)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    itemSnapshot.ref.removeValue()
                }
                Toast.makeText(this@CartActivity, "Товар удалён", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CartActivity, "Ошибка удаления", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateEmptyState() {
        if (cartItems.isEmpty()) {
            binding.emptyStateText.visibility = View.VISIBLE
            binding.cartRecyclerView.visibility = View.GONE
        } else {
            binding.emptyStateText.visibility = View.GONE
            binding.cartRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun updateTotalPrice(total: Double) {
        val formatted = String.format(Locale.US, "%,.2f ₽", total)
        val formattedForLocale = formatted.replace('.', ',')
        val totalPriceView = binding.totalPrice
        if (totalPriceView is TextView) {
            totalPriceView.text = formattedForLocale
        } else {
            Toast.makeText(this, "Ошибка: totalPrice не TextView", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseRef.removeEventListener(cartListener)
    }
}



