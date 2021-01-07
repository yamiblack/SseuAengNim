package com.jaktongdan.android.sseuaengnim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jaktongdan.android.sseuaengnim.databinding.ActivityAddBoardBinding
import java.util.*

class AddBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAddBoardSubmit.setOnClickListener {
            if(binding.editTextAddBoardName.text.isNullOrBlank()
                    || binding.editTextAddBoardDescription.text.isNullOrBlank()) {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                kFirestore.collection(Firestore.BOARD.name).add(
                        hashMapOf(
                                "name" to binding.editTextAddBoardName.text.toString(),
                                "owner" to kFirestore.collection(Firestore.MEMBER.name).document(kAuth.currentUser!!.uid),
                                "date" to Date(),
                                "description" to binding.editTextAddBoardDescription.text.toString()
                        )
                ).addOnSuccessListener {
                    finish()
                }.addOnFailureListener { e ->
                    Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}