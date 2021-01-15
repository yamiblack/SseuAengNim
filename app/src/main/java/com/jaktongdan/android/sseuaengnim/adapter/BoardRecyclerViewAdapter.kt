package com.jaktongdan.android.sseuaengnim.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.jaktongdan.android.sseuaengnim.Firestore
import com.jaktongdan.android.sseuaengnim.PostListActivity
import com.jaktongdan.android.sseuaengnim.databinding.ItemCommunityBoardBinding
import com.jaktongdan.android.sseuaengnim.kTimeText
import com.jaktongdan.android.sseuaengnim.model.BoardData

class BoardRecyclerViewAdapter(options: FirestoreRecyclerOptions<BoardData>)
    : FirestoreRecyclerAdapter<BoardData, BoardRecyclerViewAdapter.ItemViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemCommunityBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model: BoardData) {
        model.id = snapshots.getSnapshot(position).id
        holder.bindData(model, position)
    }

    inner class ItemViewHolder(private val binding: ItemCommunityBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(board: BoardData, position: Int) {
            binding.apply {
                textBoardName.text = board.name
                textBoardDescription.text = board.description

                snapshots.getSnapshot(position).reference.collection(Firestore.POST.name).orderBy("date", Query.Direction.DESCENDING)
                        .get().addOnSuccessListener {
                            textBoardCurrentDate.text = if(it.isEmpty) "" else kTimeText(it.first().getDate("date")!!)
                }

                cardBoard.setOnClickListener {
                    root.context.startActivity(Intent(root.context, PostListActivity::class.java)
                            .putExtra("board", board.id)
                            .putExtra("name", board.name)
                            .putExtra("description", board.description))
                }
            }
        }
    }
}