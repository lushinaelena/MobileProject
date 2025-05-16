package com.example.mobileproject.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileproject.Adapter.BestSellerAdapter
import com.example.mobileproject.Adapter.CategoryAdapter
import com.example.mobileproject.Model.CategoryModel          
import com.example.mobileproject.R
import com.example.mobileproject.ViewModel.MainViewModel
import com.example.mobileproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel = MainViewModel()
    private lateinit var binding: ActivityMainBinding

    // --- поиск ---
    private lateinit var categoryAdapter: CategoryAdapter
    private var originalCategoryList: List<CategoryModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* инициализация данных */
        initCategories()
        initBestSeller()
        setupSearch()

        /* нижнее меню */
        binding.bottomNavigation.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.navigation_cart -> {
                    startActivity(Intent(this, CartActivity::class.java)); true
                }
                R.id.navigation_favorite -> {
                    startActivity(Intent(this, FavoriteActivity::class.java)); true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java)); true
                }
                else -> false
            }
        }
    }

    /* ------------------- Best-seller ------------------- */
    private fun initBestSeller() {
        binding.progressBarBestSeller.visibility = View.VISIBLE
        viewModel.Горячее.observe(this) { bestList ->
            binding.viewBestSeller.apply {
                layoutManager = LinearLayoutManager(
                    this@MainActivity, LinearLayoutManager.VERTICAL, false
                )
                adapter = BestSellerAdapter(bestList)
            }
            binding.progressBarBestSeller.visibility = View.GONE
        }
    }

    /* ------------------- Categories ------------------- */
    private fun initCategories() {
        binding.progressBarCategory.visibility = View.VISIBLE
        viewModel.category.observe(this, Observer { categoryList ->
            originalCategoryList = categoryList          // сохранили «оригинал»
            categoryAdapter = CategoryAdapter(categoryList)
            binding.viewCategory.apply {
                layoutManager = LinearLayoutManager(
                    this@MainActivity, LinearLayoutManager.HORIZONTAL, false
                )
                adapter = categoryAdapter
            }
            binding.progressBarCategory.visibility = View.GONE
        })
    }

    /* ------------------- Search ------------------- */
    private fun setupSearch() {
        binding.editTextText2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int, count: Int
            ) {
                filterCategories(s.toString())
            }
        })
    }

    private fun filterCategories(query: String) {
        if (!::categoryAdapter.isInitialized) return

        val filtered = if (query.isBlank()) {
            originalCategoryList                       
        } else {
            originalCategoryList.filter {
                it.title.contains(query, ignoreCase = true)
            }
        }
        categoryAdapter.updateList(filtered)            
    }
}
