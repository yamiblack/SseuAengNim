package com.jaktongdan.android.sseuaengnim.ui.community

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.jaktongdan.android.sseuaengnim.AddBoardActivity
import com.jaktongdan.android.sseuaengnim.Firestore
import com.jaktongdan.android.sseuaengnim.R
import com.jaktongdan.android.sseuaengnim.adapter.BoardRecyclerViewAdapter
import com.jaktongdan.android.sseuaengnim.databinding.FragmentCommunityBinding
import com.jaktongdan.android.sseuaengnim.kFirestore
import com.jaktongdan.android.sseuaengnim.model.BoardData

class CommunityFragment : Fragment() {
    private lateinit var communityViewModel: CommunityViewModel
    private lateinit var binding: FragmentCommunityBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        communityViewModel = ViewModelProvider(this).get(CommunityViewModel::class.java)
        binding = FragmentCommunityBinding.inflate(inflater)

        setHasOptionsMenu(true)

        val query = kFirestore.collection(Firestore.BOARD.name)
        val options = FirestoreRecyclerOptions.Builder<BoardData>().setQuery(query, BoardData::class.java).build()
        val boardRecyclerViewAdapter = BoardRecyclerViewAdapter(options)

        binding.recyclerViewBoardList.adapter = boardRecyclerViewAdapter
        binding.recyclerViewBoardList.layoutManager = LinearLayoutManager(context)

        boardRecyclerViewAdapter.startListening()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_community, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.addBoard -> startActivity(Intent(context, AddBoardActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }
}