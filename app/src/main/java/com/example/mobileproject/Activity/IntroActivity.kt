package com.example.mobileproject.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileproject.databinding.ActivityIntroBinding
import com.example.mobileproject.MainActivity

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.startBtn.setOnClickListener {
            startActivity(Intent(this@IntroActivity, MainActivity::class.java))
        }
    }
}