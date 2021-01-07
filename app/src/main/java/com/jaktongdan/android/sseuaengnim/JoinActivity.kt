package com.jaktongdan.android.sseuaengnim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.jaktongdan.android.sseuaengnim.databinding.ActivityJoinBinding
import java.util.regex.Pattern

class JoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val correctMap = hashMapOf(
                "email" to false,
                "password" to false,
                "confirm" to false,
                "nickname" to false
        )

        supportActionBar?.title = "회원가입"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.editTextJoinEmail.addTextChangedListener {
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()) {
                binding.labelJoinEmailInfo.setTextColor(getColor(R.color.joinWarning))
                binding.labelJoinEmailInfo.text = "잘못된 형식입니다."
                correctMap["email"] = false
            } else {
                binding.labelJoinEmailInfo.setTextColor(getColor(R.color.joinCorrect))
                binding.labelJoinEmailInfo.text = "올바른 형식입니다."
                correctMap["email"] = true
                binding.buttonJoinSubmit.isEnabled = !correctMap.containsValue(false)
            }
        }

        binding.editTextJoinPassword.addTextChangedListener {
            if(!Pattern.matches("^(?=.*[0-9])(?=.*[!@#$%^&?-_~])(?=.*[a-zA-Z]).{8,}$", it.toString())) {
                binding.labelJoinPasswordInfo.setTextColor(getColor(R.color.joinWarning))
                binding.labelJoinPasswordInfo.text = "잘못된 형식입니다."
                correctMap["password"] = false
            } else {
                binding.labelJoinPasswordInfo.setTextColor(getColor(R.color.joinCorrect))
                binding.labelJoinPasswordInfo.text = "올바른 형식입니다."
                correctMap["password"] = true
                binding.buttonJoinSubmit.isEnabled = !correctMap.containsValue(false)
            }
        }

        binding.editTextJoinPasswordConfirm.addTextChangedListener {
            if(it.toString() != binding.editTextJoinPassword.text.toString()) {
                binding.labelJoinPasswordConfirmInfo.setTextColor(getColor(R.color.joinWarning))
                binding.labelJoinPasswordConfirmInfo.text = "비밀번호가 일치하지 않습니다."
                correctMap["confirm"] = false
            } else {
                binding.labelJoinPasswordConfirmInfo.setTextColor(getColor(R.color.joinCorrect))
                binding.labelJoinPasswordConfirmInfo.text = "비밀번호가 일치합니다."
                correctMap["confirm"] = true
                binding.buttonJoinSubmit.isEnabled = !correctMap.containsValue(false)
            }
        }

        binding.editTextJoinNickname.addTextChangedListener {
            when {
                it?.length ?: 0 > 10 -> {
                    binding.labelJoinNicknameInfo.setTextColor(getColor(R.color.joinWarning))
                    binding.labelJoinNicknameInfo.text = "닉네임이 너무 깁니다."
                    correctMap["nickname"] = false
                }
                it.isNullOrBlank() -> {
                    binding.labelJoinNicknameInfo.setTextColor(getColor(R.color.joinWarning))
                    binding.labelJoinNicknameInfo.text = "닉네임을 입력해주세요."
                    correctMap["nickname"] = false
                }
                else -> {
                    binding.labelJoinNicknameInfo.setTextColor(getColor(R.color.joinCorrect))
                    binding.labelJoinNicknameInfo.text = "사용 가능한 닉네임입니다."
                    correctMap["nickname"] = true
                    binding.buttonJoinSubmit.isEnabled = !correctMap.containsValue(false)
                }
            }
        }

        binding.buttonJoinSubmit.setOnClickListener {
            binding.loadingLayout.root.visibility = View.VISIBLE
            window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            kAuth.createUserWithEmailAndPassword(
                    binding.editTextJoinEmail.text.toString(),
                    binding.editTextJoinPassword.text.toString()
            ).addOnCompleteListener {
                binding.loadingLayout.root.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }.addOnSuccessListener { _ ->
                startActivity(Intent(it.context, MainActivity::class.java))
                finishAffinity()
            }.addOnFailureListener { e ->
                Toast.makeText(it.context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}