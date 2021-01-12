package com.jaktongdan.android.sseuaengnim.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.jaktongdan.android.sseuaengnim.Firestore
import com.jaktongdan.android.sseuaengnim.PostDetailActivity
import com.jaktongdan.android.sseuaengnim.databinding.ItemPostListBinding
import com.jaktongdan.android.sseuaengnim.kFirestore
import com.jaktongdan.android.sseuaengnim.kStorage
import com.jaktongdan.android.sseuaengnim.model.PostData

class PostRecyclerViewAdapter(options: FirestoreRecyclerOptions<PostData>)
    : FirestoreRecyclerAdapter<PostData, PostRecyclerViewAdapter.ItemViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemPostListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model: PostData) {
        model.id = snapshots.getSnapshot(position).id
        holder.bindData(model)
    }

    inner class ItemViewHolder(private val binding: ItemPostListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(post: PostData) {
            binding.apply {
                textPostTitle.text = post.title
                textPostContent.text = post.content
                textPostDate.text = post.date.toString()
                textPostThumbs.text = post.thumbs.size.toString()

                post.writer!!.get().addOnSuccessListener { member ->
                    textPostWriter.text = member.getString("nickname") ?: ""
                    kStorage.child("profiles/${member.getString("photo")}").downloadUrl.addOnSuccessListener { uri ->
                        Glide.with(binding.root).load(uri).into(binding.imagePostProfile)
                    }
                }

                kFirestore.collection(Firestore.COMMENT.name)
                        .whereEqualTo("post", snapshots.getSnapshot(position).reference)
                        .get().addOnSuccessListener { comment ->
                            textPostComments.text = comment.size().toString()
                        }

                cardPost.setOnClickListener {
                    root.context.startActivity(Intent(root.context, PostDetailActivity::class.java)
                            .putExtra("board", snapshots.getSnapshot(0).reference.parent.parent!!.id)
                            .putExtra("post", post.id))
                }
            }
        }
    }
}