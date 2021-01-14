package com.jaktongdan.android.sseuaengnim.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jaktongdan.android.sseuaengnim.Firestore
import com.jaktongdan.android.sseuaengnim.R
import com.jaktongdan.android.sseuaengnim.databinding.PageMyPageListBinding
import com.jaktongdan.android.sseuaengnim.kAuth
import com.jaktongdan.android.sseuaengnim.kFirestore
import com.jaktongdan.android.sseuaengnim.model.PostData

class MyPageViewPagerAdapter: RecyclerView.Adapter<MyPageViewPagerAdapter.PagerViewHolder>() {
    override fun getItemCount(): Int = 2

    override fun getItemViewType(position: Int): Int {
        return R.layout.page_my_page_list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageViewPagerAdapter.PagerViewHolder {
        val binding = PageMyPageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPageViewPagerAdapter.PagerViewHolder, position: Int) {
        holder.bindData(position)
    }

    inner class PagerViewHolder(private val binding: PageMyPageListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(position: Int) {
            binding.apply {
                recyclerViewMyPage.apply {
                    when(position) {
                        0 -> {
                            val query = kFirestore.collectionGroup(Firestore.POST.name)
                                    .whereEqualTo("writer", kFirestore.collection(Firestore.MEMBER.name).document(kAuth.uid!!))
                                    .orderBy("date", Query.Direction.DESCENDING)
                            val options = FirestoreRecyclerOptions.Builder<PostData>().setQuery(query, PostData::class.java).build()
                            val listAdapter = PostRecyclerViewAdapter(options)

                            adapter = listAdapter

                            listAdapter.startListening()
                        }
                        else -> {
                            val postMap = hashMapOf<DocumentReference, ArrayList<DocumentSnapshot>>()
                            kFirestore.collection(Firestore.COMMENT.name)
                                    .whereEqualTo("writer", kFirestore.collection(Firestore.MEMBER.name).document(kAuth.uid!!))
                                    .orderBy("date", Query.Direction.DESCENDING)
                                    .get().addOnSuccessListener {
                                        it.documents.forEach { doc ->
                                            postMap[doc.getDocumentReference("post")]?.add(doc) ?: run {
                                                postMap[doc.getDocumentReference("post")!!] = arrayListOf(doc)
                                            }
                                        }

                                        adapter = MyCommentRecyclerViewAdapter(postMap)
                                    }
                        }
                    }
                    addItemDecoration(DividerItemDecoration(root.context, LinearLayout.VERTICAL))
                    layoutManager = object : LinearLayoutManager(binding.root.context) {
                        override fun canScrollVertically(): Boolean {
                            return false
                        }
                    }
                }
            }
        }
    }
}