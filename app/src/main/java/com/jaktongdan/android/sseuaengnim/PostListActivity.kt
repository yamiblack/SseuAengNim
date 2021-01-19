package com.jaktongdan.android.sseuaengnim

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.appcompat.app.AppCompatActivity
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
        binding.recyclerViewPost.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_board, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
            R.id.infoBoard -> {
                AlertDialog.Builder(binding.root.context, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                        .setTitle(intent.getStringExtra("name"))
                        .setMessage(intent.getStringExtra("description"))
                        .setNeutralButton("확인") { _, _ -> }
                        .show()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}