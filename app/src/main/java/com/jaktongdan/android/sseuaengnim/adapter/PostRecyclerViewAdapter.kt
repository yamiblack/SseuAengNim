package com.jaktongdan.android.sseuaengnim.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.jaktongdan.android.sseuaengnim.*
import com.jaktongdan.android.sseuaengnim.databinding.ItemPostListBinding
import com.jaktongdan.android.sseuaengnim.model.PostData
import com.jaktongdan.android.sseuaengnim.ui.community.PostDetailActivity

class PostRecyclerViewAdapter(options: FirestoreRecyclerOptions<PostData>, val searchOption: Int = 0, val searchText: String? = null)
    : FirestoreRecyclerAdapter<PostData, PostRecyclerViewAdapter.ItemViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemPostListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model: PostData) {
        model.id = snapshots.getSnapshot(position).id
        holder.bindData(model, position)
    }

    inner class ItemViewHolder(private val binding: ItemPostListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(post: PostData, position: Int) {
            binding.run {
                searchText?.let { text ->
                    if( when (searchOption) {
                        0 -> !post.title.contains(text)
                        1 -> !post.content.contains(text)
                        else -> !post.title.contains(text) && !post.content.contains(text)
                    }) {
                        binding.root.removeAllViews()
                        return
                    }
                }

                textPostTitle.text = post.title
                textPostContent.text = post.content
                textPostDate.text = kTimeText(post.date)
                textPostThumbs.text = post.thumbs.size.toString()

                post.writer!!.get().addOnSuccessListener { member ->
                    textPostWriter.text = member.getString("nickname") ?: ""
                    member.getString("photo")?.let { photo ->
                        kStorage.child("profiles/$photo").downloadUrl.addOnSuccessListener { uri ->
                            Glide.with(root).load(uri).into(imagePostProfile)
                        }
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