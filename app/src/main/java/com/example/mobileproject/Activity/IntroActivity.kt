package com.example.mobileproject.Activity

import android.content.Intent
import android.os.Bundle
import com.example.mobileproject.databinding.ActivityIntroBinding
import com.google.firebase.auth.FirebaseAuth

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.startBtn.setOnClickListener {

            FirebaseAuth.getInstance().signOut()


            startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
            finish()
        }

    }
}