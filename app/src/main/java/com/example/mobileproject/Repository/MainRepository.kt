package com.example.mobileproject.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mobileproject.Model.CategoryModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainRepository {

    private val firebaseDatabase=FirebaseDatabase.getInstance()

    fun loadCategory():LiveData<MutableList<CategoryModel>>{
        val categoryLiveData=MutableLiveData<MutableList<CategoryModel>>()
        val ref=firebaseDatabase.getReference("Категория")
        
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists= mutableListOf<CategoryModel>()
                for(chilldSnapschot in snapshot.children){
                    val list=chilldSnapschot.getValue(CategoryModel::class.java)
                    if(list!=null){
                        lists.add(list)
                    }
                }
                categoryLiveData.value=lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return categoryLiveData
    }
}