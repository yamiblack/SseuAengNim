package com.jaktongdan.android.sseuaengnim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.jaktongdan.android.sseuaengnim.databinding.ActivityPostWriteBinding
import java.util.*

class PostWriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostWriteBinding
    private val boardId by lazy { intent.getStringExtra("board")!! }
    private val postId by lazy { intent.getStringExtra("post") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("post")?.let { post ->
            kFirestore.collection(Firestore.BOARD.name).document(boardId)
                    .collection(Firestore.POST.name).document(post)
                    .get().addOnSuccessListener { doc ->
                        binding.editTextPostTitle.setText(doc.getString("title"))
                        binding.editTextPostContent.setText(doc.getString("content"))
                    }
        }

        supportActionBar?.title = "글 작성"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_post_write, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
            R.id.writePost -> {
                binding.loadingLayout.root.visibility = View.VISIBLE
                window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                postId?.let { post ->
                    kFirestore.collection(Firestore.BOARD.name).document(boardId)
                            .collection(Firestore.POST.name).document(post).update(
                                    hashMapOf(
                                            "title" to binding.editTextPostTitle.text.toString(),
                                            "content" to binding.editTextPostContent.text.toString(),
                                            "writer" to kFirestore.collection(Firestore.MEMBER.name).document(kAuth.uid!!)
                                    )
                            ).addOnSuccessListener {
                                finish()
                            }.addOnFailureListener { e ->
                                binding.loadingLayout.root.visibility = View.GONE
                                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
                            }
                } ?: run {
                    kFirestore.collection(Firestore.BOARD.name).document(boardId)
                            .collection(Firestore.POST.name).add(
                                    hashMapOf(
                                            "title" to binding.editTextPostTitle.text.toString(),
                                            "content" to binding.editTextPostContent.text.toString(),
                                            "writer" to kFirestore.collection(Firestore.MEMBER.name).document(kAuth.uid!!),
                                            "date" to Date()
                                    )
                            ).addOnSuccessListener {
                                finish()
                            }.addOnFailureListener { e ->
                                binding.loadingLayout.root.visibility = View.GONE
                                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
                            }
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}