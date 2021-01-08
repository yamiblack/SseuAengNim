package com.jaktongdan.android.sseuaengnim.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.jaktongdan.android.sseuaengnim.Firestore
import com.jaktongdan.android.sseuaengnim.R
import com.jaktongdan.android.sseuaengnim.databinding.ItemCommentBinding
import com.jaktongdan.android.sseuaengnim.kAuth
import com.jaktongdan.android.sseuaengnim.model.CommentData

class CommentRecyclerViewAdapter(options: FirestoreRecyclerOptions<CommentData>)
    : FirestoreRecyclerAdapter<CommentData, CommentRecyclerViewAdapter.ItemViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model: CommentData) {
        model.id = snapshots.getSnapshot(position).id
        holder.bindData(model, position)
    }

    inner class ItemViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(comment: CommentData, position: Int) {
            binding.apply {
                comment.writer!!.get().addOnSuccessListener {
                    textCommentNickname.text = it.getString("nickname")
                }

                textCommentContent.text = comment.content
                textCommentThumbs.text = comment.thumbs.size.toString()

                if(comment.thumbs.contains(kAuth.uid!!)) {
                    iconCommentThumbs.setImageDrawable(
                            getDrawable(binding.root.context, R.drawable.ic_baseline_thumb_up_24)
                    )
                }

                if(comment.writer.id == kAuth.uid!!) {
                    iconCommentDelete.visibility = View.VISIBLE
                    iconCommentDelete.setOnClickListener {
                        AlertDialog.Builder(binding.root.context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                                .setMessage("댓글을 삭제하시겠습니까?")
                                .setPositiveButton("예") { _, _ ->
                                    snapshots.getSnapshot(position).reference.delete()
                                }
                                .setNegativeButton("아니오") { _, _ -> }
                                .show()
                    }
                    iconCommentThumbs.setImageDrawable(
                            getDrawable(binding.root.context, R.drawable.ic_baseline_thumb_up_24)
                    )
                }
            }
        }
    }
}