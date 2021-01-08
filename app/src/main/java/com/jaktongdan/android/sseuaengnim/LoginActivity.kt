package com.jaktongdan.android.sseuaengnim

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.jaktongdan.android.sseuaengnim.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.editTextLoginPassword.typeface = Typeface.DEFAULT

        binding.buttonLoginSubmit.setOnClickListener {
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.editTextLoginEmail.text.toString()).matches()) {
                binding.textInputLayout.error = "올바른 이메일 형식이 아닙니다."
            } else {
                binding.loadingLayout.root.visibility = View.VISIBLE
                window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                kAuth.signInWithEmailAndPassword(
                        binding.editTextLoginEmail.text.toString(),
                        binding.editTextLoginPassword.text.toString()
                ).addOnCompleteListener {
                    binding.loadingLayout.root.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }.addOnSuccessListener { result ->
                    kPreference(this).edit().putBoolean(
                            Settings.AUTOLOGIN.id,
                            binding.switchLoginAuto.isChecked
                    ).putString(
                            Settings.NICKNAME.id,
                            result.user!!.displayName
                    ).apply()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }.addOnFailureListener {
                    binding.textInputLayout.error = " "
                    binding.textInputLayout2.error = it.localizedMessage
                }
            }
        }

        binding.buttonLoginJoin.setOnClickListener {
            startActivity(Intent(this, JoinActivity::class.java))
        }
    }
}