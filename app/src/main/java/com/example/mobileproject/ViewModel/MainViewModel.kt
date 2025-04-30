package com.example.mobileproject.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mobileproject.Model.CategoryModel
import com.example.mobileproject.Repository.MainRepository

class MainViewModel:ViewModel() {
    private val repository=MainRepository()

    val category:LiveData<MutableList<CategoryModel>> = repository.loadCategory()
}