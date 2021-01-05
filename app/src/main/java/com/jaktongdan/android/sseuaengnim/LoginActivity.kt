package com.jaktongdan.android.sseuaengnim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jaktongdan.android.sseuaengnim.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.buttonLoginSubmit.setOnClickListener {

        }
    }
}