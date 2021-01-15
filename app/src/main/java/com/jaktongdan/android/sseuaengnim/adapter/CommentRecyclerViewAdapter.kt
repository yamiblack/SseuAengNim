package com.jaktongdan.android.sseuaengnim.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.jaktongdan.android.sseuaengnim.R
import com.jaktongdan.android.sseuaengnim.databinding.ActivityPostDetailBinding
import com.jaktongdan.android.sseuaengnim.databinding.ItemCommentBinding
import com.jaktongdan.android.sseuaengnim.kAuth
import com.jaktongdan.android.sseuaengnim.kStorage
import com.jaktongdan.android.sseuaengnim.kTimeText
import com.jaktongdan.android.sseuaengnim.model.CommentData

class CommentRecyclerViewAdapter(options: FirestoreRecyclerOptions<CommentData>, val parentBinding: ActivityPostDetailBinding)
    : FirestoreRecyclerAdapter<CommentData, CommentRecyclerViewAdapter.ItemViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        updateCommentCount()
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model: CommentData) {
        model.id = snapshots.getSnapshot(position).id
        holder.bindData(model, position)
    }

    private fun updateCommentCount() {
        val commentCount = "댓글($itemCount)"
        parentBinding.textPostDetailCommentCount.text = commentCount
    }

    inner class ItemViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(comment: CommentData, position: Int) {
            binding.apply {
                comment.writer!!.get().addOnSuccessListener {
                    textCommentNickname.text = it.getString("nickname")
                    kStorage.child("profiles/${it.getString("photo")}").downloadUrl.addOnSuccessListener { uri ->
                        Glide.with(binding.root).load(uri).into(binding.imageCommentProfile)
                    }
                }

                textCommentContent.text = comment.content
                textCommentThumbs.text = comment.thumbs.size.toString()
                textCommentDate.text = kTimeText(comment.date)

                if (comment.thumbs.contains(kAuth.uid!!)) {
                    iconCommentThumbs.setImageDrawable(
                            getDrawable(binding.root.context, R.drawable.ic_baseline_thumb_up_24)
                    )
                }

                if (comment.writer.id == kAuth.uid!!) {
                    iconCommentDelete.visibility = View.VISIBLE
                    iconCommentDelete.setOnClickListener {
                        AlertDialog.Builder(binding.root.context, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                                .setMessage("댓글을 삭제하시겠습니까?")
                                .setPositiveButton("예") { _, _ ->
                                    snapshots.getSnapshot(position).reference.delete().addOnSuccessListener {
                                        updateCommentCount()
                                    }
                                }
                                .setNegativeButton("아니오") { _, _ -> }
                                .show()
                    }
                    iconCommentThumbs.setImageDrawable(
                            getDrawable(binding.root.context, R.drawable.ic_baseline_thumb_up_24)
                    )
                } else {
                    binding.iconCommentThumbs.setOnClickListener {
                        if (comment.thumbs.contains(kAuth.uid!!)) {
                            snapshots.getSnapshot(position).reference.update("thumbs", mutableListOf<String>().apply {
                                addAll(comment.thumbs)
                                remove(kAuth.uid!!)
                            })
                        } else {
                            snapshots.getSnapshot(position).reference.update("thumbs", mutableListOf<String>().apply {
                                addAll(comment.thumbs)
                                add(kAuth.uid!!)
                            })
                        }
                    }
                }
            }
        }
    }
}