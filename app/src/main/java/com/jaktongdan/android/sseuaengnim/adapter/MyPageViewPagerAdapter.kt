package com.jaktongdan.android.sseuaengnim.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.jaktongdan.android.sseuaengnim.*
import com.jaktongdan.android.sseuaengnim.databinding.PageMyPageListBinding
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
                    val query = if (position == 0) kFirestore.collectionGroup(Firestore.POST.name)
                            .whereEqualTo("writer", kFirestore.collection(Firestore.MEMBER.name).document(kAuth.uid!!))
                            .orderBy("date", Query.Direction.DESCENDING)
                                else kFirestore.collection(Firestore.COMMENT.name)
                            .whereEqualTo("writer", kFirestore.collection(Firestore.MEMBER.name).document(kAuth.uid!!))
                            .orderBy("date", Query.Direction.DESCENDING)
                    val options = FirestoreRecyclerOptions.Builder<PostData>().setQuery(query, PostData::class.java).build()
                    val listAdapter = PostRecyclerViewAdapter(options)

                    addItemDecoration(DividerItemDecoration(binding.root.context, LinearLayout.VERTICAL))
                    adapter = listAdapter
                    layoutManager = LinearLayoutManager(binding.root.context)

                    listAdapter.startListening()
                }
            }
        }
    }
}