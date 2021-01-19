package com.jaktongdan.android.sseuaengnim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.jaktongdan.android.sseuaengnim.adapter.PostRecyclerViewAdapter
import com.jaktongdan.android.sseuaengnim.databinding.ActivityPostListBinding
import com.jaktongdan.android.sseuaengnim.model.PostData

class PostListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val boardId = intent.getStringExtra("board")!!
        val board = kFirestore.collection(Firestore.BOARD.name).document(boardId)

        board.addSnapshotListener { value, _ ->
            supportActionBar?.title = value!!.getString("name")
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val query = board.collection(Firestore.POST.name).orderBy("date", Query.Direction.DESCENDING)
        val options = FirestoreRecyclerOptions.Builder<PostData>().setQuery(query, PostData::class.java).build()
        val postRecyclerViewAdapter = PostRecyclerViewAdapter(options)

        binding.recyclerViewPost.adapter = postRecyclerViewAdapter
        binding.recyclerViewPost.layoutManager = LinearLayoutManager(this)

        postRecyclerViewAdapter.startListening()

        binding.fabPostList.setOnClickListener {
            startActivity(Intent(this, PostWriteActivity::class.java)
                    .putExtra("board", boardId))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.recyclerViewPost.adapter?.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}