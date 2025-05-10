package com.example.mobileproject.Activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileproject.Adapter.ListAdapter
import com.example.mobileproject.ViewModel.MainViewModel
import com.example.mobileproject.databinding.ActivityListItemsBinding

class ListItemsActivity : AppCompatActivity() {
    lateinit var binding: ActivityListItemsBinding
    private var viewModel= MainViewModel()
    private var id:String=""
    private var title:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityListItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBundle()
        initList()

    }
    private fun initList(){
        binding.apply {
            progressBar2.visibility= View.VISIBLE
            viewModel.loadПредметы(id).observe(this@ListItemsActivity, Observer {
                listview.layoutManager = LinearLayoutManager(this@ListItemsActivity, LinearLayoutManager.VERTICAL,false)
                listview.adapter = ListAdapter(it)
                progressBar2.visibility=View.GONE
            })
            backButt.setOnClickListener { finish() }
        }
    }

    private fun getBundle(){
        id=intent.getStringExtra("id")!!
        title=intent.getStringExtra("title")!!
        binding.kategoriaTxt.text=title
    }
}