package com.jaktongdan.android.sseuaengnim.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.jaktongdan.android.sseuaengnim.*
import com.jaktongdan.android.sseuaengnim.databinding.ItemMyCommentBinding
import com.jaktongdan.android.sseuaengnim.model.PostData
import com.jaktongdan.android.sseuaengnim.ui.community.PostDetailActivity

class MyCommentRecyclerViewAdapter(data: HashMap<DocumentReference, ArrayList<DocumentSnapshot>>)
    : RecyclerView.Adapter<MyCommentRecyclerViewAdapter.ItemViewHolder>() {
    private val comments = data.toList().sortedBy {
        it.second.first().getDate("date")
    }

    override fun getItemCount(): Int = comments.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemMyCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindData(comments[position])
    }

    inner class ItemViewHolder(val binding: ItemMyCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(comment: Pair<DocumentReference, ArrayList<DocumentSnapshot>>) {
            binding.run {
                comment.first.get().addOnSuccessListener { doc ->
                    val post = doc.toObject(PostData::class.java)!!

                    originalPost.run {
                        textPostTitle.text = post.title
                        textPostContent.text = post.content
                        textPostDate.text = kTimeText(post.date)
                        textPostThumbs.text = post.thumbs.size.toString()

                        post.writer!!.get().addOnSuccessListener { member ->
                            textPostWriter.text = member.getString("nickname") ?: ""
                            kStorage.child("profiles/${member.getString("photo")}").downloadUrl.addOnSuccessListener { uri ->
                                Glide.with(root).load(uri).into(imagePostProfile)
                            }
                        }

                        kFirestore.collection(Firestore.COMMENT.name)
                                .whereEqualTo("post", comment.first)
                                .get().addOnSuccessListener { comments ->
                                    textPostComments.text = comments.size().toString()
                                }

                        cardPost.setOnClickListener {
                            root.context.startActivity(Intent(root.context, PostDetailActivity::class.java)
                                    .putExtra("board", comment.first.parent.parent!!.id)
                                    .putExtra("post", comment.first.id))
                        }
                    }
                }

                recyclerViewMyComment.adapter = MyCommentCommentRecyclerViewAdapter(comment.second)
                recyclerViewMyComment.layoutManager = object : LinearLayoutManager(root.context) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
            }
        }
    }
}