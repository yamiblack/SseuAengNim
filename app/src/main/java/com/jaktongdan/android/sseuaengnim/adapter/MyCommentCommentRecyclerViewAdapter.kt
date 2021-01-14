package com.jaktongdan.android.sseuaengnim.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.jaktongdan.android.sseuaengnim.databinding.ItemMyCommentCommentBinding
import com.jaktongdan.android.sseuaengnim.kTimeText

class MyCommentCommentRecyclerViewAdapter(private val comments: List<DocumentSnapshot>)
    : RecyclerView.Adapter<MyCommentCommentRecyclerViewAdapter.ItemViewHolder>() {
    override fun getItemCount(): Int = comments.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemMyCommentCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindData(comments[position])
    }

    inner class ItemViewHolder(val binding: ItemMyCommentCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(comment: DocumentSnapshot) {
            binding.run {
                textMyCommentContent.text = comment.getString("content")
                textMyCommentDate.text = kTimeText(comment.getDate("date")!!)
            }
        }
    }
}