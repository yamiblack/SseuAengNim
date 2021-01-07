package com.jaktongdan.android.sseuaengnim.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.jaktongdan.android.sseuaengnim.databinding.ItemCommunityBoardBinding
import com.jaktongdan.android.sseuaengnim.model.BoardData

class BoardRecyclerViewAdapter(options: FirestoreRecyclerOptions<BoardData>)
    : FirestoreRecyclerAdapter<BoardData, BoardRecyclerViewAdapter.ItemViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemCommunityBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model: BoardData) {
        model.id = snapshots.getSnapshot(position).id
        holder.bindData(model)
    }

    inner class ItemViewHolder(private val binding: ItemCommunityBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(board: BoardData) {
            binding.apply {
                textBoardName.text = board.name
                textBoardDescription.text = board.description
                //TODO : TextBoardCurrentDate.text = ?
            }
        }
    }
}