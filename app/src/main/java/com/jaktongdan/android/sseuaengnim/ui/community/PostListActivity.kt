package com.jaktongdan.android.sseuaengnim.ui.community

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.jaktongdan.android.sseuaengnim.Firestore
import com.jaktongdan.android.sseuaengnim.R
import com.jaktongdan.android.sseuaengnim.adapter.PostRecyclerViewAdapter
import com.jaktongdan.android.sseuaengnim.databinding.ActivityPostListBinding
import com.jaktongdan.android.sseuaengnim.kFirestore
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

        binding.buttonPostSearch.setOnClickListener {
            postRecyclerViewAdapter.stopListening()

            val recyclerViewAdapter = PostRecyclerViewAdapter(options, binding.spinnerPostSearch.selectedItemPosition, binding.editTextPostSearch.text.toString())
            binding.recyclerViewPost.adapter = recyclerViewAdapter
            recyclerViewAdapter.startListening()
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
            R.id.searchPost -> {
                binding.layoutPostSearch.visibility = if(binding.layoutPostSearch.visibility == View.GONE) View.VISIBLE else View.GONE
            }
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