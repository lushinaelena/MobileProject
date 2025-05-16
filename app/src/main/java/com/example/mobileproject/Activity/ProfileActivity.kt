package com.example.mobileproject.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileproject.Model.ProfileModel
import com.example.mobileproject.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var profileListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "Пользователь не авторизован", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Подключение к профилю в базе
        databaseRef = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(userId)
            .child("Profile")

        // Показываем email из FirebaseAuth
        binding.profileEmail.text = auth.currentUser?.email ?: "Email не найден"

        // Загрузка профиля
        loadProfile()

        // Кнопка назад
        binding.backBtn.setOnClickListener {
            finish()
        }

        // Кнопка редактирования имени
        binding.editProfileBtn.setOnClickListener {
            showEditNameDialog()
        }

        // Кнопка выхода
        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun loadProfile() {
        profileListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profile = snapshot.getValue(ProfileModel::class.java)
                binding.profileName.text = profile?.name?.ifEmpty { "Имя не задано" } ?: "Имя не задано"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, "Ошибка загрузки: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

        databaseRef.addValueEventListener(profileListener)
    }

    private fun showEditNameDialog() {
        val editText = EditText(this)
        editText.hint = "Введите новое имя"

        AlertDialog.Builder(this)
            .setTitle("Редактировать имя")
            .setView(editText)
            .setPositiveButton("Сохранить") { _, _ ->
                val newName = editText.text.toString().trim()
                if (newName.isNotEmpty()) {
                    val updatedProfile = ProfileModel(name = newName, email = auth.currentUser?.email ?: "")
                    databaseRef.setValue(updatedProfile)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Имя обновлено", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Ошибка при обновлении", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Имя не может быть пустым", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::profileListener.isInitialized) {
            databaseRef.removeEventListener(profileListener)
        }
    }
}

