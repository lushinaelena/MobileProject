package com.example.mobileproject.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mobileproject.Model.CategoryModel
import com.example.mobileproject.Model.ItemsModel
import com.example.mobileproject.Repository.MainRepository

class MainViewModel:ViewModel() {
    private val repository=MainRepository()

    val category:LiveData<MutableList<CategoryModel>> = repository.loadCategory()
    val Горячее:LiveData<MutableList<ItemsModel>> = repository.loadГорячее()
    fun loadПредметы(categoryId:String):LiveData<MutableList<ItemsModel>> {
        return repository.loadПредметы(categoryId)
    }
}