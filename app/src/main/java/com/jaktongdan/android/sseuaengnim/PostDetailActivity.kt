package com.jaktongdan.android.sseuaengnim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.jaktongdan.android.sseuaengnim.adapter.CommentRecyclerViewAdapter
import com.jaktongdan.android.sseuaengnim.databinding.ActivityPostDetailBinding
import com.jaktongdan.android.sseuaengnim.model.CommentData
import java.util.*

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val boardId = intent.getStringExtra("board")!!
        val postId = intent.getStringExtra("post")!!
        val post = kFirestore.collection(Firestore.BOARD.name).document(boardId)
                .collection(Firestore.POST.name).document(postId)

        post.addSnapshotListener { value, _ ->
            supportActionBar?.title = value!!.getString("title")

            binding.apply {
                textPostDetailTitle.text = value.getString("title")
                textPostDetailContent.text = value.getString("content")
                textPostDetailThumbs.text = (value["thumbs"] as List<*>? ?: listOf<String>()).size.toString()
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val query = kFirestore.collection(Firestore.COMMENT.name)
                .whereEqualTo("post", post)
                .orderBy("date")
        val options = FirestoreRecyclerOptions.Builder<CommentData>().setQuery(query, CommentData::class.java).build()
        val commentRecyclerViewAdapter = CommentRecyclerViewAdapter(options)

        binding.recyclerViewPostDetailComment.adapter = commentRecyclerViewAdapter
        binding.recyclerViewPostDetailComment.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        commentRecyclerViewAdapter.startListening()

        binding.buttonPostDetailSubmit.setOnClickListener {
            if(binding.editTextPostDetailComment.text.isNotBlank()) {
                kFirestore.collection(Firestore.COMMENT.name).add(
                        hashMapOf(
                                "post" to post,
                                "content" to binding.editTextPostDetailComment.text.toString(),
                                "writer" to kFirestore.collection(Firestore.MEMBER.name).document(kAuth.uid!!),
                                "date" to Date()
                        )
                ).addOnSuccessListener {
                    binding.editTextPostDetailComment.text.clear()
                }.addOnFailureListener { e ->
                    Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
                }
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