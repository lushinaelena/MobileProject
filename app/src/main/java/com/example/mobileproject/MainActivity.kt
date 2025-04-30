package com.example.mobileproject

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileproject.Adapter.CategoryAdapter
import com.example.mobileproject.ViewModel.MainViewModel
import com.example.mobileproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel = MainViewModel()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initCategories()
        }

    private fun initCategories() {
        binding.progressBarCategory.visibility=View.VISIBLE
        viewModel.category.observe(this, Observer {
            binding.viewCategory.layoutManager =
                LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
            binding.viewCategory.adapter=CategoryAdapter(it)
            binding.progressBarCategory.visibility = View.GONE
        })
    }
}